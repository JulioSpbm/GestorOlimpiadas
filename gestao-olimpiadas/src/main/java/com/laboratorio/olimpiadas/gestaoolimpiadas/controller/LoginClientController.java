package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.util.Base64;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginClientController {
    @FXML
    private AnchorPane loginView;
    @FXML
    private TextField lblEmailClient;
    @FXML
    private PasswordField lblPasswordClient;
    @FXML
    private Hyperlink registerClientPage;
    /**
     * Valida os dados do cliente e navega para a página de clientes se o login for bem-sucedido.
     */
    @FXML
    private void onLogin() {
        String email = lblEmailClient.getText();
        String password = lblPasswordClient.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erro", "Por favor, preencha todos os campos.", AlertType.ERROR);
            return;
        }

        String username = "EG3";
        String userPassword = "7?bo?PL03S";
        String encodedAuth = Base64.getEncoder().encodeToString((username + ":" + userPassword).getBytes());

        String jsonBody = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://services.inapa.com/opo/api/client/login/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + encodedAuth)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                // Converte para JSON
                JSONObject jsonResponse = new JSONObject(responseBody);

                // Verifica se a resposta contém a chave "Client"
                if (jsonResponse.has("Client")) {
                    JSONArray clientArray = jsonResponse.getJSONArray("Client");

                    if (clientArray.length() > 0) {
                        JSONObject clientData = clientArray.getJSONObject(0);
                        String clientId = clientData.getString("Id");
                        String clientEmail = clientData.getString("Email");

                        // Armazena os dados do cliente na sessão
                        SessionManager.getInstance().setClientData(clientId, clientEmail);

                        showAlert("Sucesso", "Login realizado com sucesso!", AlertType.INFORMATION);
                        goToClientsPage();
                    } else {
                        showAlert("Erro", "Nenhum cliente encontrado na resposta do servidor.", AlertType.ERROR);
                    }
                } else {
                    showAlert("Erro", "Resposta inválida do servidor.", AlertType.ERROR);
                }

            } else {
                showAlert("Erro ao efetuar login:",
                        "Status: " + response.statusCode() + "\nMensagem: " + response.body(),
                        AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erro", "Ocorreu um erro ao tentar efetuar o login. Tente novamente.", AlertType.ERROR);
        }
    }
    /**
     * Método chamado ao clicar no link de registo.
     * Navega para a página de registo do cliente.
     */
    @FXML
    private void goToRegisterPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/client_registration.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) registerClientPage.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Registo do Cliente");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar a página de registo.", AlertType.ERROR);
        }
    }
    /**
     * Método que direciona para a página de clientes após um login bem-sucedido.
     */
    private void goToClientsPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/clients_page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) lblEmailClient.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Página de Clientes");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro", "Não foi possível carregar a página de clientes.", AlertType.ERROR);
        }
    }
    /**
     * Exibe um alerta com título, mensagem e tipo especificado.
     *
     * @param title   Título do alerta.
     * @param message Mensagem do alerta.
     * @param alertType Tipo de alerta (erro, sucesso, etc).
     */
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onGestorPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/login-view.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        closeCurrentWindow();
    }

    private void closeCurrentWindow() {
        Stage currentStage = (Stage) loginView.getScene().getWindow();
        currentStage.close();
    }
}
