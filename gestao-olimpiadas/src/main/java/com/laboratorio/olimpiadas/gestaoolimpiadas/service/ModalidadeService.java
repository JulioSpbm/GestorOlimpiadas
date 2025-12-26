package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.ModalidadeDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.UtilizadorDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AuthenticateUserUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.XMLImporterUtils;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalInt;

/**
 * Serviço para operações de negócio relacionadas à Modalidade.
 * Contém métodos que servem como camada intermediária entre o controlador e o DAO, validando dados antes de os enviar para a base de dados.
 */
public class ModalidadeService {

    private static ModalidadeDAO modalidadeDAO = null;

    public ModalidadeService() {
        this.modalidadeDAO = new ModalidadeDAO();
    }

    /**
     * Insere uma nova modalidade com os dados especificados.
     * Realiza validações nos dados de entrada antes de chamar o DAO para inserir a modalidade na base de dados.
     *
     * @param tipo O tipo da modalidade (individual ou equipa).
     * @param genero O género da modalidade (masculino ou feminino).
     * @param nome O nome da modalidade.
     * @param descricao A descrição da modalidade.
     * @param minParticipantes O número mínimo de participantes.
     * @param idPontuacao A pontuação associada à modalidade.
     * @param numJogos O número de jogos.
     * @param regras As regras da modalidade.
     * @return true se a modalidade foi inserida com sucesso; false caso contrário.
     */
    public static boolean insertModalidade(String tipo, String genero, String nome, String descricao, String minParticipantes, String idPontuacao, String numJogos, String regras) {

        boolean tipoo = tipo.equalsIgnoreCase("Individual");
        boolean generoo = genero.equalsIgnoreCase("Masculino");
        int minParticipantess = Integer.parseInt(minParticipantes);
        if (minParticipantess != 2){
            AlertUtils.showErrorAlert("Erro", "A modalidade não pode ter menos do que dois participantes");
            return false;
        }

        int numJogoss = Integer.parseInt(numJogos);
        if (minParticipantess < 1){
            AlertUtils.showErrorAlert("Erro", "A modalidade não pode ter menos do que um jogo.");
            return false;
        }

        OptionalInt idPontuacaoOptional = ModalidadeDAO.getScoreIdByName(idPontuacao);
        if (idPontuacaoOptional.isEmpty()) {
            throw new IllegalArgumentException("Pontuação não encontrada: " + idPontuacao);
        }
        int idPontuacaoo = idPontuacaoOptional.getAsInt();

        Modalidade modalidade = new Modalidade(tipoo, generoo, nome, descricao, minParticipantess, idPontuacaoo, numJogoss, regras);

        return ModalidadeDAO.createModalidade(modalidade);
    }

    /**
     * Edita a Modalidade.
     *
     * @param id
     * @param tipo
     * @param genero
     * @param nome
     * @param descricao
     * @param minParticipantes
     * @param idPontuacao
     * @param numJogos
     * @param regras
     * @param estado
     * @return
     */
    public static boolean editModalidade(int id, String tipo, String genero, String nome, String descricao, String minParticipantes, String idPontuacao, String numJogos, String regras, String estado) {

        boolean tipoo = tipo.equalsIgnoreCase("Coletivo");
        boolean generoo = genero.equalsIgnoreCase("Masculino");
        int minParticipantess = Integer.parseInt(minParticipantes);
        int numJogoss = Integer.parseInt(numJogos);

        OptionalInt idPontuacaoOptional = ModalidadeDAO.getScoreIdByName(idPontuacao);
        if (idPontuacaoOptional.isEmpty()) {
            throw new IllegalArgumentException("Pontuação não encontrada: " + idPontuacao);
        }
        int idPontuacaoo = idPontuacaoOptional.getAsInt();

        Modalidade modalidade = new Modalidade(tipoo, generoo, nome, descricao, minParticipantess, idPontuacaoo, numJogoss, regras, estado);



        return ModalidadeDAO.editModalidade(modalidade, id);
    }

    /**
     *  Importa a lista das modalidades
     *
     * @param modalidades
     * @param stage
     */
    public static void importModalidades(ObservableList<Modalidade> modalidades, Stage stage) {
        ProgressBar progressBar = new ProgressBar(0);
        VBox layout = new VBox(10, progressBar);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Alert progressAlert = new Alert(Alert.AlertType.NONE);
        progressAlert.setTitle("Importação em progresso");
        progressAlert.setHeaderText("Por favor, aguarde enquanto os dados são importados...");
        progressAlert.getDialogPane().setContent(layout);
        progressAlert.initOwner(stage);

        Task<Void> importTask = new Task<>() {
            @Override
            protected Void call() throws SQLException {
                int total = modalidades.size();
                for (int i = 0; i < total; i++) {
                    Modalidade modalidade = modalidades.get(i);
                    ModalidadeDAO.createModalidadeXML(modalidade);

                    String utilizador = modalidade.getRecordeOlimpico().getUtilizadorNome();
                    String equipa = modalidade.getRecordeOlimpico().getEquipaNome();



                    if (utilizador != null && !utilizador.isEmpty()) {
                        equipa = null;
                    } else if (equipa != null && !equipa.isEmpty()) {
                        utilizador = null;
                    }
                    String medalhas = modalidade.getRecordeOlimpico().getNum_medalhasNome();
                    String record = modalidade.getRecordeOlimpico().getRecord_olimpicoNome();

                    int num_medalhas = 0;
                    float record_olimpico = 0;
                    if (medalhas != null && !medalhas.isEmpty()) {
                        num_medalhas = Integer.parseInt(medalhas);
                    } else if (record != null && !record.isEmpty() && record.contains(":")) {
                        String[] partes = record.split(":");

                        if (partes.length == 3) {

                            try {
                                int horas = Integer.parseInt(partes[0]);
                                int minutos = Integer.parseInt(partes[1]);
                                String[] segundosParte = partes[2].split("\\."); // Divide segundos de milissegundos
                                int segundos = Integer.parseInt(segundosParte[0]); // Segundos
                                int milissegundos = segundosParte.length > 1 ? Integer.parseInt(segundosParte[1]) : 0; // Milissegundos
                                record_olimpico = horas * 3600 + minutos * 60 + segundos + milissegundos / 100.0f;
                            } catch (NumberFormatException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    int ano = modalidade.getRecordeOlimpico().getAno_recorde();

                    RecordeModalidade recordeOlimpico = new RecordeModalidade(utilizador,equipa,num_medalhas,record_olimpico,ano);
                    modalidadeDAO.createRecordOlimpico(recordeOlimpico);

                    String utilizadorVencedor = modalidade.getVencedorOlimpico().getUtilizadorNome();
                    String equipaVencedor = modalidade.getVencedorOlimpico().getEquipaNome();
                    if (utilizadorVencedor != null && !utilizadorVencedor.isEmpty()) {
                        equipaVencedor = null;
                    } else if (equipaVencedor != null && !equipaVencedor.isEmpty()) {
                        utilizadorVencedor = null;
                    }

                    String medalhaVencedor = modalidade.getVencedorOlimpico().getMedalhaNome();
                    String resultadoVencedor = modalidade.getVencedorOlimpico().getResultadoNome();

                    int id_medalhaVencedor = 0;
                    float vencedor_olimpico = 0;

                    if (medalhas != null && !medalhas.isEmpty()) {
                        id_medalhaVencedor = ResultadoService.getIdMedalhaByName(medalhaVencedor);
                    } else if (record != null && !record.isEmpty() && record.contains(":")) {
                        String[] partes2 = resultadoVencedor.split(":");

                        if (partes2.length == 3) {

                            try {
                                int horas = Integer.parseInt(partes2[0]);
                                int minutos = Integer.parseInt(partes2[1]);
                                String[] segundosParte = partes2[2].split("\\.");
                                int segundos = Integer.parseInt(segundosParte[0]);
                                int milissegundos = segundosParte.length > 1 ? Integer.parseInt(segundosParte[1]) : 0;
                                vencedor_olimpico = horas * 3600 + minutos * 60 + segundos + milissegundos / 100.0f;
                            } catch (NumberFormatException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }

                    int anoVencedor = modalidade.getVencedorOlimpico().getAno_vencedor();


                    VencedorOlimpico vencedorOlimpico = new VencedorOlimpico(utilizadorVencedor,equipaVencedor,id_medalhaVencedor,vencedor_olimpico,anoVencedor);
                    modalidadeDAO.createVencedorOlimpico(vencedorOlimpico);

                    updateProgress(i + 1, total);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(importTask.progressProperty());
        ButtonType cancelButton = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        progressAlert.getButtonTypes().add(cancelButton);

        progressAlert.setOnCloseRequest(event -> {
            if (progressBar.progressProperty().get() < 1) {
                importTask.cancel();
                AlertUtils.showErrorAlert("Importação Cancelada", "A importação foi cancelada.");
            }
        });

        importTask.setOnSucceeded(event -> {
            int id = 0;
            progressAlert.close();
            AlertUtils.showInfoAlert("Sucesso!", "Importação concluída!");
            String numMecanografico = AuthenticateUserUtils.getNumMecanografico();
            try {
                id = UtilizadorDAO.getIdUserByNumMecanografico(numMecanografico);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            XMLImporterUtils.salvarHistoricoXmlModalidades(id);
        });

        importTask.setOnFailed(event -> {
            progressAlert.close();
            AlertUtils.showErrorAlert("Erro!", "Importação falhou!");
        });

        progressAlert.show();

        new Thread(importTask).start();
    }


    /**
     * Method to list all Modalidade records
     */
    public List<Modalidade> listarModalidades() {
        return modalidadeDAO.listarModalidades();
    }

    public List<Modalidade> listarModalidadesSemEvento() {
        return modalidadeDAO.listarModalidadesSemEvento();
    }

    public void addModalidadeEquipa(int idModalidade, int idEquipa) {
        modalidadeDAO.addModalidadeEquipa(idModalidade,idEquipa);
    }
}
