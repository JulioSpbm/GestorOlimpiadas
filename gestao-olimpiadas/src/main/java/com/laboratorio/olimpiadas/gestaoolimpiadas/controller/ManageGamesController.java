package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Prova;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ManageGamesController {
    private static final String USERNAME = "EG3";
    private static final String PASSWORD = "7?bo?PL03S";

    public AnchorPane gameCreatePane;
    @FXML
    private TableView<Prova> tableViewProvas;
    @FXML
    private TableColumn<Prova, Date> colStartDateProva;
    @FXML
    private TableColumn<Prova, Date> colEndDateProva;
    @FXML
    private TableColumn<Prova, String> colLocalProva;
    @FXML
    private TableColumn<Prova, String> colSportProva;
    @FXML
    private TableColumn<Prova, Integer> colCapacityProva;

    @FXML
    private TextField fldLocal;
    @FXML
    private TextField fldSport;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TextField fldCapacidade;
    @FXML
    private TextField fldEventId;


    private final ObservableList<Prova> provasList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (tableViewProvas != null) { // Só inicializa a tabela se ela existir no FXML carregado
            colStartDateProva.setCellValueFactory(new PropertyValueFactory<>("startdate"));
            colEndDateProva.setCellValueFactory(new PropertyValueFactory<>("enddate"));
            colLocalProva.setCellValueFactory(new PropertyValueFactory<>("local"));
            colSportProva.setCellValueFactory(new PropertyValueFactory<>("sport"));
            colCapacityProva.setCellValueFactory(new PropertyValueFactory<>("capacity"));

            try {
                List<Prova> provas = Prova_get.fetchProvas();
                ClientesController.fetchGameTickets(provas);
                provasList.addAll(provas);
                tableViewProvas.setItems(provasList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class Prova_get {
        private static final String BASE_URL = "https://services.inapa.com/opo/api/game";

        public static List<Prova> fetchProvas() throws Exception {
            // Criar cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Adicionar autenticação Basic Auth
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

                responseBody = responseBody.trim();
                if (responseBody.startsWith("[") && responseBody.endsWith("]")) {
                    responseBody = responseBody.substring(1, responseBody.length() - 1);
                }

                String[] jsonObjects = responseBody.split("\\},\\{");

                List<Prova> provas = new ArrayList<>();

                DateTimeFormatter[] dateFormats = {
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH")
                };

                for (String jsonObject : jsonObjects) {
                    jsonObject = jsonObject.replace("{", "").replace("}", "");

                    String[] keyValuePairs = jsonObject.split(",");

                    Prova prova = new Prova();
                    for (String pair : keyValuePairs) {
                        String[] keyValue = pair.split(":");
                        String key = keyValue[0].trim().replace("\"", "");
                        String value = keyValue[1].trim().replace("\"", "");

                        switch (key) {
                            case "StartDate":
                                prova.setStartdate(parseDate(value, dateFormats));
                                break;
                            case "EndDate":
                                prova.setEnddate(parseDate(value, dateFormats));
                                break;
                            case "Location":
                                prova.setLocal(value);
                                break;
                            case "Sport":
                                prova.setSport(value);
                                break;
                            case "Capacity":
                                prova.setCapacity(Integer.parseInt(value));
                                break;
                        }
                    }
                    provas.add(prova);
                }

                return provas;
            } else {
                throw new RuntimeException("Erro ao conectar: Código " + response.statusCode());
            }
        }

        private static Date parseDate(String dateString, DateTimeFormatter[] formats) {
            DateTimeFormatter exactFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            try {
                return java.sql.Timestamp.valueOf(LocalDateTime.parse(dateString, exactFormat));
            } catch (Exception e) {
                for (DateTimeFormatter format : formats) {
                    try {
                        return java.sql.Timestamp.valueOf(LocalDateTime.parse(dateString, format));
                    } catch (Exception ignored) {
                    }
                }
            }
            throw new RuntimeException("Formato de data inválido: " + dateString);
        }

    }

    @FXML
    private void btnAddGame() {

        try {
            String local = fldLocal.getText();
            String sport = fldSport.getText();
            String startDate = dpStartDate.getValue().toString() + "T10:30:00";
            String endDate = dpEndDate.getValue().toString() + "T11:30:00";
            int capacity = Integer.parseInt(fldCapacidade.getText());
            int eventId = Integer.parseInt(fldEventId.getText()); // Capturando EventId

            String jsonBody = String.format(
                    "{\"StartDate\":\"%s\",\"EndDate\":\"%s\", \"Location\":\"%s\", \"Capacity\":%d, \"Sport\":\"%s\", \"Active\":\"true\",\"EventId\":%d}",
                    startDate, endDate, local, capacity, sport, eventId
            );

            HttpClient client = HttpClient.newHttpClient();
            String auth = USERNAME + ":" + PASSWORD;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            String authorizationHeader = "Basic " + encodedAuth;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://services.inapa.com/opo/api/game"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorizationHeader)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                AlertUtils.showInfoAlert("Info", "Prova criada com sucesso!");
            } else {
                AlertUtils.showWarningAlert("Erro", "Falha ao criar prova!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCreateGame() {
        try {
                openNewWindow("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/create_game.fxml");
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }
    private Stage openNewWindow(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
        return newStage;
    }
}
