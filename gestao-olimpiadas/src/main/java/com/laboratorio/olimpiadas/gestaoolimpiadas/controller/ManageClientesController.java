package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Cliente;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ManageClientesController {
    private static final String USERNAME = "EG3";
    private static final String PASSWORD = "7?bo?PL03S";

    @FXML
    private TableView<Cliente> tableViewClientes;
    @FXML
    private TableColumn<Cliente, String> colNomeCliente;
    @FXML
    private TableColumn<Cliente, String> colEmailCliente;
    @FXML
    private TableColumn<Cliente, Void> colTicketsCliente;

    private final ObservableList<Cliente> clientesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNomeCliente.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmailCliente.setCellValueFactory(new PropertyValueFactory<>("email"));
        addButtonToTable();

        try {
            List<Cliente> clientes = ClientGet.fetchClientes();
            clientesList.addAll(clientes);
            tableViewClientes.setItems(clientesList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableViewClientes.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (cliente == null || empty) {
                    setStyle("");
                } else if (!cliente.isActive()) {
                    setStyle("-fx-background-color: #FFCCCC;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    public static class ClientGet {
        private static final String BASE_URL = "https://services.inapa.com/opo/api/client";

        public static List<Cliente> fetchClientes() throws Exception {
            HttpClient client = HttpClient.newHttpClient();

            // Configuração da autenticação Basic Auth
            String auth = USERNAME + ":" + PASSWORD;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authorizationHeader = "Basic " + encodedAuth;

            // Construir a requisição HTTP
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL))
                    .header("Accept", "application/json")
                    .header("Authorization", authorizationHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                return parseClientes(responseBody);
            } else {
                throw new RuntimeException("Erro ao conectar: Código " + response.statusCode());
            }
        }
        private static List<Cliente> parseClientes(String responseBody) {
            List<Cliente> clientes = new ArrayList<>();

            // Primeiro, encontra a lista de clientes dentro do campo "Clients"
            String[] jsonObjects = responseBody
                    .substring(responseBody.indexOf("[") + 1, responseBody.lastIndexOf("]"))
                    .split("\\},\\{");

            for (String jsonObject : jsonObjects) {
                jsonObject = jsonObject.replace("{", "").replace("}", "");

                String[] keyValuePairs = jsonObject.split(",");
                Cliente cliente = new Cliente();

                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim().replace("\"", "");

                    if (key.equalsIgnoreCase("Id")) {
                        cliente.setId(value);
                    } else if (key.equalsIgnoreCase("Name")) {
                        cliente.setName(value);
                    } else if (key.equalsIgnoreCase("Email")) {
                        cliente.setEmail(value);
                    } else if (key.equalsIgnoreCase("Active")) {
                    cliente.setActive(Boolean.parseBoolean(value));
                }
                }
                clientes.add(cliente);
            }
            return clientes;
        }
    }

    private void addButtonToTable() {
        colTicketsCliente.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("View Tickets");

            {
                btn.setOnAction(event -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());

                    if (cliente == null || cliente.getId() == null || cliente.getId().isEmpty()) {
                        System.err.println("Erro: O ID do cliente é nulo ou vazio!");
                        return;
                    }

                    String clientId = cliente.getId();

                    try {
                        Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);
                        loadingAlert.setTitle("Loading");
                        loadingAlert.setHeaderText(null);
                        loadingAlert.setContentText("Verificando tickets...");
                        loadingAlert.show();

                        if (hasTickets(clientId)) {
                            loadingAlert.close();

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/clients-tickets.fxml"));
                            Parent root = loader.load();

                            ManageTicketController ticketController = loader.getController();
                            ticketController.setClientId(clientId);

                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } else {
                            loadingAlert.close();

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("No Tickets");
                            alert.setHeaderText(null);
                            alert.setContentText("This client has no tickets.");
                            alert.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Cliente cliente = getTableView().getItems().get(getIndex());

                    // Desativar o botão se o cliente estiver banido
                    if (cliente != null) {
                        btn.setDisable(!cliente.isActive() || cliente.getId() == null || cliente.getId().isEmpty());
                    } else {
                        btn.setDisable(true);
                    }

                    setGraphic(btn);
                }
            }
        });
    }

    private boolean hasTickets(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            System.err.println("Erro: clientId está nulo ou vazio!");
            return false;
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            String auth = USERNAME + ":" + PASSWORD;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authorizationHeader = "Basic " + encodedAuth;

            HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://services.inapa.com/opo/api/ticket/client/" + clientId))
                    .header("Accept", "application/json")
                    .header("Authorization", authorizationHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 && !response.body().trim().isEmpty() && !response.body().equals("[]");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void btnClientBan() {
        Cliente clienteSelecionado = tableViewClientes.getSelectionModel().getSelectedItem();

        if (clienteSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nenhum Cliente Selecionado");
            alert.setHeaderText(null);
            alert.setContentText("Selecione um cliente antes de banir.");
            alert.showAndWait();
            return;
        }

        String clientId = clienteSelecionado.getId();
        String url = "https://services.inapa.com/opo/api/client/" + clientId;

        try {
            HttpClient client = HttpClient.newHttpClient();
            String auth = USERNAME + ":" + PASSWORD;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authorizationHeader = "Basic " + encodedAuth;

            String requestBody = "{\"Active\": false}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Accept", "application/json")
                    .header("Authorization", authorizationHeader)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                clienteSelecionado.setActive(false);
                AlertUtils.showInfoAlert("Info", "Client Ban!");
                tableViewClientes.refresh();
            } else {
                throw new RuntimeException("Erro ao banir cliente: Código " + response.statusCode() + " - " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao banir cliente");
            alert.setContentText("Ocorreu um erro ao tentar banir o cliente. Verifique a conexão e tente novamente.");
            alert.showAndWait();
        }
    }
}
