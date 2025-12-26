package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.Base64;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

public class RegisterClientController {
    @FXML
    private TextField lblNameClientRegister;
    @FXML
    private TextField lblEmailClientRegister;
    @FXML
    private PasswordField lblPasswordClientRegister;
    @FXML
    private Hyperlink loginClientPage;
    /**
     * Método chamado ao clicar no link de registo.
     * Valida os campos e envia a requisição para registar o cliente.
     */
    @FXML
    private void onLogin() {
        String name = lblNameClientRegister.getText();
        String email = lblEmailClientRegister.getText();
        String password = lblPasswordClientRegister.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Erro", "Todos os campos são obrigatórios.", AlertType.ERROR);
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erro", "Por favor, insira um email válido no formato: exemplo@gmail.com.", AlertType.ERROR);
            return;
        }

        String username = "EG3";
        String userPassword = "7?bo?PL03S";
        String encodedAuth = Base64.getEncoder().encodeToString((username + ":" + userPassword).getBytes());

        // Cria o JSON para enviar na requisição e o cliente HTTP
        String jsonBody = String.format("{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}", name, email, password);

        HttpClient client = HttpClient.newHttpClient();

        // Cria a requisição POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://services.inapa.com/opo/api/client/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Envia a requisição e processa a resposta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                showAlert("Sucesso", "Cliente registado com sucesso!", AlertType.INFORMATION);
                goToClientsPage();
            } else {
                showAlert("Erro ao registrar cliente:",
                        "Status: " + response.statusCode() + "\nMensagem: " + response.body(),
                        AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro durante o registo. Tente novamente.", AlertType.ERROR);
        }
    }
    /**
     * Valida o formato do email.
     *
     * @param email O email a ser validado.
     * @return true se o email for válido, caso contrário, false.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
    // Redireciona para o Login
    @FXML
    private void goToLoginPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/client_login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) loginClientPage.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Login do Cliente");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar a página de login.", Alert.AlertType.ERROR);
        }
    }
    // Redireciona para a página de clientes após o registo bem-sucedido
    private void goToClientsPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/clients_page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) lblNameClientRegister.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Página de Clientes");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar a página de clientes.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
