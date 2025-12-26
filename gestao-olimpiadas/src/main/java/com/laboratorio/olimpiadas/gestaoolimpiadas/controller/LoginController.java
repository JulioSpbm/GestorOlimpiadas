package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.LoginDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controlador responsável pelo processo de login da aplicação.
 * Este controlador permite logar o utilizador ao verificar
 * o seu nome e palavra-passe fornecidos.
 */
public class LoginController {

    @FXML
    private AnchorPane loginView;

    @FXML
    private TextField lblUsername;

    @FXML
    private PasswordField lblPassword;

    /**
     * Verifica se os campos de nome e palavra-passe estão preenchidos,
     * autentica o utilizador e exibe alertas com base no resultado da autenticação.
     *
     * @throws SQLException se ocorrer um erro de acesso à base de dados.
     * @throws IOException se ocorrer um erro ao carregar a nova janela.
     */
    @FXML
    public void onLogin() throws SQLException, IOException {
        String num_mecanografico = lblUsername.getText();
        String password = lblPassword.getText();

        if (num_mecanografico.isEmpty() || password.isEmpty()) {
            AlertUtils.showWarningAlert("Aviso", "Preencha todos os campos.");
            return;
        }

        Integer userType = LoginDAO.authenticate(num_mecanografico, password);
        if (userType != null) {
            if (userType == -1) {
                AlertUtils.showWarningAlert("Aviso", "Bem-vindo utilizador! \nPor favor, altere a palavra-passe.");
                openChangePasswordWindow(num_mecanografico);
                return;
            }
            handleSuccessfulLogin(userType, num_mecanografico);
        } else {
            AlertUtils.showErrorAlert("Erro", "Nome de utilizador ou palavra-passe incorretos.");
        }
    }

    /**
     * Carrega A página da password.
     * @param num_mecanografico
     * @throws IOException
     * @throws SQLException
     */
    private void openChangePasswordWindow(String num_mecanografico) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/change_password.fxml"));
        Parent root = loader.load();

        // Passar o identificador para o controlador da tela de alteração de senha
        ChangePasswordController controller = loader.getController();
        controller.setAuthenticatedUser(num_mecanografico, 0);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        // Fechar a janela atual de login
        closeCurrentWindow();
    }

    /**
     * Carrega A página do cliente.
     */
    @FXML
    private void onClienteLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/client_login.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        closeCurrentWindow();
    }

    /**
     * Lida com o login bem-sucedido, exibindo mensagens e carregando a
     * interface correspondente com base no tipo de utilizador.
     *
     * @param userType o tipo de utilizador autenticado.
     * @param num_mecanografico o nome de utilizador autenticado.
     * @throws IOException se ocorrer um erro ao carregar a nova janela.
     */
    private void handleSuccessfulLogin(int userType, String num_mecanografico) throws IOException, SQLException {
        String roleMessage = userType == 1 ? "Admin" : "Atleta";
        String fxmlPath = userType == 1
                ? "/com/laboratorio/olimpiadas/gestaoolimpiadas/views/manager.fxml"
                : "/com/laboratorio/olimpiadas/gestaoolimpiadas/views/athlete.fxml";

        AlertUtils.showInfoAlert("Login Bem-sucedido", "Bem-vindo " + roleMessage + ", " + num_mecanografico + "!");

        openNewWindow(fxmlPath,num_mecanografico);
        closeCurrentWindow();
    }

    /**
     * Abre uma nova janela com o caminho especificado.
     *
     * @param fxmlPath o caminho do ficheiro FXML da nova janela.
     * @throws IOException se ocorrer um erro ao carregar o ficheiro FXML.
     */
    private void openNewWindow(String fxmlPath, String num_mecanografico) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Object controller = loader.getController();
        if (controller instanceof GestorController) {
            ((GestorController) controller).setAuthenticatedUser(num_mecanografico);
        } else if (controller instanceof UtilizadorController) {
            ((UtilizadorController) controller).setAuthenticatedUser(num_mecanografico);
            ((UtilizadorController) controller).loadFotoPerfil();
            ((UtilizadorController) controller).carregarRecorde();
        }

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));

        if (fxmlPath.contains("manager.fxml")) {
            newStage.setTitle("Gestor - Gestão de Olimpíadas");
        } else if (fxmlPath.contains("athlete.fxml")) {
            newStage.setTitle("Atleta - Gestão de Olimpíadas");
        }

        newStage.show();
    }


    /**
     * Fecha a janela atual.
     */
    private void closeCurrentWindow() {
        Stage currentStage = (Stage) loginView.getScene().getWindow();
        currentStage.close();
    }
}