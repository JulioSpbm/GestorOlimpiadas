package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.AtletaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.UtilizadorDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.AtletaRecorde;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Utilizador;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AuthenticateUserUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.GenMecanografico;
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
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio relacionada ao Atleta.
 */
public class AtletaService {
    private static final AtletaDAO atletaDAO = new AtletaDAO();

    /**
     * Insere um novo atleta na base de dados.
     * @param nome Nome do atleta.
     * @param pais País do atleta.
     * @param genero Género do atleta.
     * @param altura Altura do atleta em centímetros.
     * @param peso Peso do atleta em quilogramas.
     * @param dataNascimento Data de nascimento do atleta.
     * @return true se o atleta for inserido com sucesso, caso contrário false.
     * @throws SQLException Se ocorrer um erro ao aceder à base de dados.
     */
    public static boolean insertAtletas(String nome, String pais, String genero, String altura, String peso, String dataNascimento) throws SQLException, IOException {

        if (!isValidDate(dataNascimento)) {
            AlertUtils.showErrorAlert("Erro", "Data inválida. Insira uma data no formato YYYY-MM-DD.");
            return false;
        }

        LocalDate dataNascimentoParsed = LocalDate.parse(dataNascimento);
        LocalDate hoje = LocalDate.now();

        if (dataNascimentoParsed.isAfter(hoje)) {
            AlertUtils.showErrorAlert("Erro", "A data de nascimento não pode ser no futuro.");
            return false;
        }

        int idade = Period.between(dataNascimentoParsed, hoje).getYears();
        if (idade < 16) {
            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 16 anos.");
            return false;
        }

        int pesoo = Integer.parseInt(peso);
        if (pesoo < 20) {
            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 20 kilos.");
            return false;
        }

        int alturaa = Integer.parseInt(altura);
        if (alturaa < 100) {
            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 1 metro.");
            return false;
        }

        int idPais = atletaDAO.getPaisIdByName(pais);
        if (idPais == -1) {
            AlertUtils.showErrorAlert("Aviso!", "Nenhum país encontrado com o nome: " + pais);
            return false;
        }
        Date dataNascimentoo = Date.valueOf(dataNascimento);
        boolean generoo = genero.equalsIgnoreCase("Feminino");

        Participacao participacao = new Participacao(0, 0, 0, 0);
        String username = GenMecanografico.generateNextMecanografico(2);
        Utilizador utilizador = new Utilizador(username, username, 2);

        Atleta atleta = new Atleta(nome, idPais, generoo, alturaa, pesoo, dataNascimentoo, participacao, utilizador);

        return atletaDAO.createAtleta(atleta);
    }

    /**
     * Atualiza um atleta na base de dados.
     *
     * @param id             ID do atleta.
     * @param nome           Nome do atleta.
     * @param pais           País do atleta.
     * @param genero         Género do atleta.
     * @param altura         Altura do atleta em centímetros.
     * @param peso           Peso do atleta em quilogramas.
     * @param dataNascimento Data de nascimento do atleta.
     * @param estadoAtleta
     * @return true se o atleta for atualizado com sucesso, caso contrário false.
     * @throws SQLException Se ocorrer um erro ao aceder à base de dados.
     */

    public static boolean updateAtleta(Integer id, String nome, String pais, String genero, String altura, String peso, String dataNascimento, String estadoAtleta) throws SQLException, IOException {
        if (!isValidDate(dataNascimento)) {
            AlertUtils.showErrorAlert("Erro", "Data inválida. Insira uma data no formato YYYY-MM-DD.");
            return false;
        }

        LocalDate dataNascimentoParsed = LocalDate.parse(dataNascimento);
        LocalDate hoje = LocalDate.now();

        if (dataNascimentoParsed.isAfter(hoje)) {
            AlertUtils.showErrorAlert("Erro", "A data de nascimento não pode ser no futuro.");
            return false;
        }

        int idade = Period.between(dataNascimentoParsed, hoje).getYears();
        if (idade < 16) {
            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 16 anos.");
            return false;
        }

        int pesoo = Integer.parseInt(peso);
        if (pesoo < 20) {
            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 20 kilos.");
            return false;
        }

        int alturaa = Integer.parseInt(altura);
        if (alturaa < 100) {
            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 1 metro.");
            return false;
        }


        int idPais = atletaDAO.getPaisIdByName(pais);
        if (idPais == -1) {
            AlertUtils.showErrorAlert("Aviso!", "Nenhum país encontrado com o nome: " + pais);
            return false;
        }
        Date dataNascimentoo = Date.valueOf(dataNascimento);
        boolean generoo = genero.equalsIgnoreCase("Feminino");

        Participacao participacao = new Participacao(0, 0, 0, 0);
        String username = GenMecanografico.generateNextMecanografico(2);
        Utilizador utilizador = new Utilizador(username, username, 2);
        Atleta atleta = new Atleta(id,nome, idPais, generoo, altura, pesoo, dataNascimentoo, participacao, utilizador, estadoAtleta);

        return atletaDAO.updateAtleta(atleta);
    }


    /**
     * Importa novos atletas na base de dados.
     * @param atletas Lista de Atletas.
     * @param stage Stage atual.
     */

    public static void importAtletas(ObservableList<Atleta> atletas, Stage stage) {
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
            protected Void call() {
                try {
                    int total = atletas.size();
                    for (int i = 0; i < total; i++) {
                        Atleta atleta = atletas.get(i);

                        String username = GenMecanografico.generateNextMecanografico(2);
                        Utilizador utilizador = new Utilizador(username, username, 2);
                        atleta.setUtilizador(utilizador);

                        if (atleta.getPeso() < 20) {
                            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 20 kilos.");
                            throw new RuntimeException("Erro durante a importação!");
                        }

                        if (atleta.getAltura() < 100) {
                            AlertUtils.showErrorAlert("Erro", "O atleta deve ter pelo menos 1 metro.");
                            throw new RuntimeException("Erro durante a importação!");
                        }

                        boolean generoo = atleta.getGeneroNome().equalsIgnoreCase("Women");
                        atleta.setGenero(generoo);

                        int idPais = atletaDAO.getPaisIdByIsoName(atleta.getPaisNome());
                        if(idPais == -1){
                            AlertUtils.showErrorAlert("Aviso!", "Nenhum país encontrado com o iso: " + atleta.getPaisNome());
                            throw new RuntimeException("Erro durante a importação!");
                        }
                        atleta.setidPais(idPais);

                        if(!atletaDAO.createAtleta(atleta)){
                            AlertUtils.showErrorAlert("Erro!", "Erro ao inserir atleta!");
                            throw new RuntimeException("Erro durante a importação!");
                        }

                        for (Participacao participacao : atleta.getParticipacoes()) {
                            if(!atletaDAO.createParticipacao(participacao)){
                                throw new RuntimeException("Erro durante a importação!");
                            }
                        }

                        updateProgress(i + 1, total);
                    }
                } catch (SQLException | IOException e) {
                    throw new RuntimeException("Erro durante a importação: " + e.getMessage());
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
            XMLImporterUtils.salvarHistoricoXmlAtleta(id);
        });

        importTask.setOnFailed(event -> {
            progressAlert.close();
            AlertUtils.showErrorAlert("Erro!", "Importação falhou!");
        });

        progressAlert.show();

        new Thread(importTask).start();
    }

    /**
     * Valida se uma data está no formato correto (YYYY-MM-DD).
     * @param dateStr String representando a data.
     * @return true se a data for válida, caso contrário false.
     */
    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtém a lista de todos os países.
     * @return Lista de países como String.
     * @throws SQLException Se ocorrer um erro ao aceder à base de dados.
     */
    public static List<String> getCountries() throws SQLException {
        return atletaDAO.getAllCountries();
    }


    /**
     * Obtém a lista de todos os atletas.
     * @return Lista de atletas como String.
     */

    public List<Atleta> listarAtletas() {
        return atletaDAO.listarAtletas();
    }

    /**
     * Obtém um atleta pelo o numero mecanografico.
     * @return Atleta.
     */

    public Atleta getAtletaByNumMecanografico(String authenticatedUser) {
        return atletaDAO.getAtletaByNumMecanografico(authenticatedUser);
    }

    public Atleta getAtletadetails(String authenticatedUser) {
        return atletaDAO.getAtletadetails(authenticatedUser);
    }

    public List<AtletaRecorde> listarHistoricoAtleta(String authenticatedUser) {
        return atletaDAO.getHistoricoAtleta(authenticatedUser);
    }


}
