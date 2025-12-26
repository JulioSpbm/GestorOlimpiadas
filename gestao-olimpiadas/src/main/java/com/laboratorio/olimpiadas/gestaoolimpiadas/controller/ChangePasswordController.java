package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.LoginDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;

public class ChangePasswordController {

    @FXML
    public PasswordField fldCurrentPassword;
    @FXML
    private PasswordField fldNewPassword;
    @FXML
    private PasswordField fldConfirmPassword;

    private String authenticatedUser;

    private int valor;

    /**
     * Atualiza o número mecanográfico e o estado de autenticação.
     * Define o utilizador autenticado no sistema.
     */
    public void setAuthenticatedUser(String numMecanografico, Integer num) {
        if (num == 0) {
            this.authenticatedUser = numMecanografico;
            this.valor = num;
        } else if (num == 1) {
            this.valor = num;
            try {
                Map<String, String> passwordData = LoginDAO.getPasswordAndSaltByNumMecanografico(numMecanografico);
                if (passwordData != null) {
                    this.authenticatedUser = numMecanografico;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Erro", "Erro ao acessar a base de dados para recuperar a senha.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Valida os campos de entrada para a palavra-passe atual.
     * - Todos os campos devem ser preenchidos.
     * - A nova palavra-passe não pode ser igual à atual.
     * - A nova palavra-passe e a confirmação devem coincidir.
     */
    @FXML
    public void onChangePassword() {
        String currentPassword = fldCurrentPassword.getText();
        String newPassword = fldNewPassword.getText();
        String confirmPassword = fldConfirmPassword.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            AlertUtils.showWarningAlert("Aviso", "Preencha todos os campos.");
            return;
        }

        if (currentPassword.equals(newPassword)) {
            AlertUtils.showErrorAlert("Erro", "A nova senha não pode ser a mesma que a atual.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AlertUtils.showErrorAlert("Erro", "As senhas não coincidem.");
            return;
        }

        try {
            if (valor == 0)
            {
                if (!LoginDAO.checkCurrentPassword(authenticatedUser, currentPassword)) {
                    AlertUtils.showErrorAlert("Erro", "Senha atual incorreta.");
                } else if (LoginDAO.updatePassword(authenticatedUser, newPassword)) {
                    AlertUtils.showInfoAlert("Sucesso", "Senha alterada com sucesso. Faça login novamente.");
                    redirectToLogin();
                } else {
                    AlertUtils.showErrorAlert("Erro", "Erro ao atualizar a senha.");
                }
            }else if (valor == 1)
            {
                if (!LoginDAO.checkPassword(authenticatedUser, currentPassword)) {
                    AlertUtils.showErrorAlert("Erro", "Senha atual incorreta.");
                }
                else if (LoginDAO.updatePassword(authenticatedUser, newPassword)) {
                    AlertUtils.showInfoAlert("Sucesso", "Senha alterada com sucesso. Faça login novamente.");
                    closeChangePassword();
                } else {
                    AlertUtils.showErrorAlert("Erro", "Erro ao atualizar a senha.");
                }
            }
        } catch (SQLException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao acessar a base de dados.");
        }
    }

    /**
     * Redireciona o utilizador para a página de login.
     * @throws IOException
     */
    private void redirectToLogin() throws IOException {
        Stage currentStage = (Stage) fldNewPassword.getScene().getWindow();
        currentStage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/login-view.fxml"));
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loader.load()));
        loginStage.show();
    }

    /**
     * Fecha a janela.
     * @throws IOException
     */
    private void closeChangePassword() throws IOException {
        Stage currentStage = (Stage) fldNewPassword.getScene().getWindow();
        currentStage.close();
    }
}
