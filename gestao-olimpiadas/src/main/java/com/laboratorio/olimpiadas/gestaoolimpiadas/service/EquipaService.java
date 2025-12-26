package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.EquipaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.UtilizadorDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
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
import java.sql.SQLException;
import java.util.List;

/**
 * Serviço para manipulação de dados de Equipa.
 */
public class EquipaService {

    private static EquipaDAO equipaDAO = new EquipaDAO();

    /**
     * Insere uma nova equipa na base de dados.
     *
     * @param nome Nome da equipa.
     * @param pais País da equipa.
     * @param modalidade Modalidade desportiva da equipa.
     * @param genero Género da equipa.
     * @param anoFundacao Ano de fundação da equipa.
     * @return true se a equipa foi inserida com sucesso, false caso contrário.
     * @throws SQLException Em caso de erro de acesso à base de dados.
     */
    public static boolean insertEquipa(String nome, String pais, String modalidade, String genero, int anoFundacao) throws SQLException{

        if (anoFundacao <= 0) {
            AlertUtils.showErrorAlert("Erro", "Ano de fundação inválido. Insira um ano positivo.");
            return false;
        }

        int idPais = equipaDAO.getPaisIdByName(pais);
        if(idPais == -1){
            AlertUtils.showErrorAlert("Aviso!", "Nenhum país encontrado com o nome: " + pais);
            return false;
        }
        int idModalidade = equipaDAO.getModalidadeIdByName(modalidade);
        if(idModalidade == -1){
            AlertUtils.showErrorAlert("Aviso!", "Nenhuma modalidade encontrada com o nome: " + modalidade);
            return false;
        }

        boolean generoo = genero.equalsIgnoreCase("Masculino");
        Participacao participacao = new Participacao(0,0,0,0);

        Equipa equipa = new Equipa(nome, idPais, generoo, idModalidade, anoFundacao, participacao);
        return equipaDAO.createEquipa(equipa);
    }

    /**
     * Importa as equipas para a base de dados.
     *
     * @param equipas
     * @param stage
     */
    public static void importTeam(ObservableList<Equipa> equipas, Stage stage) {
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
                    int total = equipas.size();
                    for (int i = 0; i < total; i++) {
                        Equipa equipa = equipas.get(i);

                        equipaDAO.createEquipaXML(equipa);

                        for (Participacao participacao : equipa.getParticipacoes()) {
                            equipaDAO.createParticipacaoTeam(participacao);
                        }

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
            XMLImporterUtils.salvarHistoricoXmlEquipas(id);
        });

        importTask.setOnFailed(event -> {
            progressAlert.close();
            AlertUtils.showErrorAlert("Erro!", "Importação falhou!");
        });

        progressAlert.show();

        new Thread(importTask).start();
    }

    /**
     * Edita os dados da equipa.
     *
     * @param nome
     * @param pais
     * @param modalidade
     * @param genero
     * @param anoFundacao
     * @return
     * @throws SQLException
     */
    public static boolean editEquipa(String nome, String pais, String modalidade, String genero, int anoFundacao) throws SQLException{

        if (anoFundacao <= 0) {
            AlertUtils.showErrorAlert("Erro", "Ano de fundação inválido. Insira um ano positivo.");
            return false;
        }

        int idPais = equipaDAO.getPaisIdByName(pais);
        int idModalidade = equipaDAO.getModalidadeIdByName(modalidade);

        boolean generoo = genero.equalsIgnoreCase("Masculino");
        Participacao participacao = new Participacao(0,0,0,0);

        Equipa equipa = new Equipa(nome, idPais, generoo, idModalidade, anoFundacao, participacao);

        int idEquipa = equipaDAO.getIdEquipaByName(nome);

        if (idEquipa == -1){
            AlertUtils.showErrorAlert("Aviso!", "Nenhuma equipa encontrada com o nome: " + nome);
            return false;
        }

        equipa.setId(idEquipa);
        return equipaDAO.editEquipa(equipa);
    }

    /**
     * Obtém a lista de modalidades disponíveis.
     *
     * @return Lista de modalidades como Strings.
     * @throws SQLException Em caso de erro de acesso à base de dados.
     */
    public static List<String> getModalidades() throws SQLException{
        return equipaDAO.getAllModalidades();
    }

    /**
     * Obtém a lista de países disponíveis.
     *
     * @return Lista de países como Strings.
     * @throws SQLException Em caso de erro de acesso à base de dados.
     */
    public static List<String> getCountries() throws SQLException {
        return equipaDAO.getAllCountries();
    }

    public static int getIdEquipaByName(String nome) throws SQLException {
        return equipaDAO.getIdEquipaByName(nome);
    }

    public static String getNameEquipaById(int id) throws SQLException, IOException {
        return equipaDAO.getNameEquipaById(id);
    }

    public List<Equipa> listarEquipas() {
        return equipaDAO.listarEquipas();
    }

    public List<Equipa> listarEquipasFiltradas(int id_modalidade) {
        return equipaDAO.listarEquipasFiltradas(id_modalidade);
    }
}