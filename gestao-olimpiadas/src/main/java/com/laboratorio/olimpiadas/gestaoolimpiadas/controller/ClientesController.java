package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Prova;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Ticket;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.SessionManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.ByteArrayInputStream;
import java.util.stream.Collectors;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class ClientesController {
    @FXML
    public Label lblName;
    @FXML
    private Pane gamesPane;
    @FXML
    private Pane profilePane;

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

    private final ObservableList<Prova> provasList = FXCollections.observableArrayList();

    private String authenticatedUserId;
    private String authenticatedUser;

    @FXML
    private TableView<Ticket> tableViewTickets;
    @FXML
    private TableColumn<Ticket, Date> colMatInic;
    @FXML
    private TableColumn<Ticket, Date> colMatFim;
    @FXML
    private TableColumn<Ticket, String> colMatLoc;
    @FXML
    private TableColumn<Ticket, String> colMatBanco;

    private final ObservableList<Ticket> ticketsList = FXCollections.observableArrayList();

    @FXML
    private ImageView qrCodeImageView;

    private static final String USERNAME = "EG3";
    private static final String PASSWORD = "7?bo?PL03S";
    private static final String BASE_URL = "https://services.inapa.com/opo/api/game";
    private static final String TICKET_URL = "https://services.inapa.com/opo/api/ticket/";
    private static final String TICKET_CLIENT_URL = "https://services.inapa.com/opo/api/ticket/client/";

    /**
     * Inicializa o controlador. Carrega as provas e configura a tabela.
     */
    @FXML
    public void initialize() {
        gamesPane.setVisible(true);
        profilePane.setVisible(false);
        setupTableView();
        loadProvas();

        authenticatedUserId = SessionManager.getInstance().getClientId();

        tableViewProvas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Prova selectedProva = tableViewProvas.getSelectionModel().getSelectedItem();
                if (selectedProva != null) {
                    confirmTicketPurchase(selectedProva);
                }
            }
        });
    }
    /**
     * Configura as colunas da tabela para exibir as provas e os bilhetes.
     */
    private void setupTableView() {
        colStartDateProva.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        colEndDateProva.setCellValueFactory(new PropertyValueFactory<>("enddate"));
        colLocalProva.setCellValueFactory(new PropertyValueFactory<>("local"));
        colSportProva.setCellValueFactory(new PropertyValueFactory<>("sport"));
        colCapacityProva.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        colMatInic.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colMatFim.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colMatLoc.setCellValueFactory(new PropertyValueFactory<>("location"));
        colMatBanco.setCellValueFactory(new PropertyValueFactory<>("seat"));
        /**
         * Exibe o QR code do bilhete ao clicar duas vezes
         */
        tableViewTickets.setRowFactory(tv -> {
            TableRow<Ticket> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Ticket clickedTicket = row.getItem();
                    showQrCodePopup(clickedTicket.getQrCodeBase64());
                }
            });
            return row;
        });
    }
    /**
     * Carrega as provas (eventos) da API e adiciona-as à tabela.
     */
    private void loadProvas() {
        try {
            List<Prova> provas = fetchProvas();
            fetchGameTickets(provas);
            provasList.addAll(provas);
            tableViewProvas.setItems(provasList);
        } catch (Exception e) {
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar as provas.");
        }
    }
    /**
     * Exibe a tela principal de jogos.
     */
    @FXML
    public void onShowHome() {
        gamesPane.setVisible(true);
        profilePane.setVisible(false);
    }
    /**
     * Exibe o perfil do cliente com os seus bilhetes.
     */
    @FXML
    public void onShowProfile() {
        gamesPane.setVisible(false);
        profilePane.setVisible(true);
        loadClientTickets(authenticatedUserId);
    }
    /**
     * Realiza o logout do cliente, fecha a aplicação atual e abre na tela de login.
     */
    @FXML
    public void onShowLogout() {
        try {
            Stage currentStage = (Stage) gamesPane.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/client_login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao tentar fazer logout: " + e.getMessage());
        }
    }
    /**
     * Abre a tela para alteração da senha do cliente.
     */
    @FXML
    public void onShowChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/laboratorio/olimpiadas/gestaoolimpiadas/views/change_password.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ChangePasswordController controller = loader.getController();
            controller.setAuthenticatedUser(this.authenticatedUser, 1);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao carregar a tela de alteração da password.");
        }
    }
    /**
     * Fetch das provas da API.
     * @return Lista as provas.
     * @throws Exception Se ocorrer erro ao pegar nas provas.
     */
    public static List<Prova> fetchProvas() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URL))
                .header("Accept", "application/json")
                .header("Authorization", authorizationHeader)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body().trim();

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray provasArray = jsonObject.getJSONArray("Games");

            List<Prova> provas = new ArrayList<>();

            for (int i = 0; i < provasArray.length(); i++) {
                JSONObject gameJson = provasArray.getJSONObject(i);

                String id = gameJson.getString("Id");
                int eventID = gameJson.getInt("EventId");

                Date startDate = parseDate(gameJson.getString("StartDate"));
                Date endDate = parseDate(gameJson.getString("EndDate"));

                Prova game = new Prova(
                        id,
                        eventID,
                        startDate,
                        endDate,
                        gameJson.getString("Location"),
                        gameJson.getString("Sport"),
                        gameJson.getInt("Capacity")
                );
                provas.add(game);
            }
            return provas;
        } else {
            throw new RuntimeException("Erro ao conectar: Código " + response.statusCode());
        }
    }
    /**
     * Converte uma string de data para o formato Date.
     * @param dateString String da data no formato "yyyy-MM-dd'T'HH:mm:ss".
     * @return Data convertida.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static Date parseDate(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, FORMATTER);
        return Timestamp.valueOf(localDateTime);
    }
    /**
     * Confirma a compra de um bilhete para a prova selecionada.
     * @param selectedProva Prova selecionada.
     */
    private void confirmTicketPurchase(Prova selectedProva) {
        if (selectedProva.getCapacity() <= 0) {
            AlertUtils.showErrorAlert("Capacidade Esgotada", "Não há mais bilhetes disponíveis.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Compra");
        alert.setHeaderText("Deseja adquirir um bilhete?");
        alert.setContentText("Local: " + selectedProva.getLocal() + "\nDesporto: " + selectedProva.getSport());

        ButtonType buttonYes = new ButtonType("Sim");
        ButtonType buttonNo = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonYes) {
                try {
                    boolean ticketPurchased = purchaseTicket(selectedProva, authenticatedUserId);
                    if (ticketPurchased) {
                        selectedProva.setCapacity(selectedProva.getCapacity() - 1);
                        tableViewProvas.refresh();
                    }
                } catch (Exception e) {
                    AlertUtils.showErrorAlert("Erro na Compra", "Erro ao tentar comprar o bilhete.");
                }
            }
        });
    }
    /**
     * Compra um bilhete para a prova selecionada.
     * @param selectedProva Prova selecionada.
     * @return Se a compra foi bem-sucedida.
     * @throws Exception Se ocorrer erro na compra.
     */
    private boolean purchaseTicket(Prova selectedProva, String clientId) throws Exception {
        if (selectedProva.getCapacity() <= 0) {
            AlertUtils.showErrorAlert("Erro", "Não é possível comprar bilhete. Capacidade esgotada!");
            return false;
        }

        HttpClient client = HttpClient.newHttpClient();

        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;

        Set<Integer> occupiedSeats = getOccupiedSeats(selectedProva.getLocal());

        int newSeat = findAvailableSeat(occupiedSeats);

        JSONObject ticketData = new JSONObject();
        ticketData.put("ClientId", clientId);
        ticketData.put("GameId", selectedProva.getId());
        ticketData.put("Seat", newSeat);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(TICKET_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", authorizationHeader)
                .POST(HttpRequest.BodyPublishers.ofString(ticketData.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            tableViewProvas.refresh();
            AlertUtils.showInfoAlert("Bilhete Adquirido!", "O seu bilhete foi comprado com sucesso. Lugar: " + newSeat);
            return true;
        } else if (response.statusCode() == 406) {
            AlertUtils.showErrorAlert("Erro", "Nenhum bilhete encontrado");
            return false;
        } else {
            AlertUtils.showErrorAlert("Erro", "Erro ao Adquirir Bilhete");
            return false;
        }
    }

    private Set<Integer> getOccupiedSeats(String provaLocation) throws Exception {
        List<Prova> provas = getProvasByLocation(provasList, provaLocation);
        List<Ticket> gameTickets = fetchGameTicket(provas);
        Set<Integer> occupiedSeats = new HashSet<>();

        for (Ticket ticket : gameTickets) {
            try {
                    occupiedSeats.add(Integer.parseInt(ticket.getSeat()));
            } catch (NumberFormatException e) {
                    System.err.println("Assento inválido para o bilhete: " + ticket.getSeat());
            }
        }

        return occupiedSeats;
    }

    public List<Prova> getProvasByLocation(ObservableList<Prova> provasList, String provaLocation) {
        return provasList.stream()
                .filter(prova -> prova.getLocal().equalsIgnoreCase(provaLocation))
                .collect(Collectors.toList());
    }

    /**
     * Método para encontrar um lugar disponível
     */
    private int findAvailableSeat(Set<Integer> occupiedSeats) {
        int seatNumber = 1;
        while (occupiedSeats.contains(seatNumber)) {
            seatNumber++;
        }
        return seatNumber;
    }


    public void setAuthenticatedUser(String userId) {
        this.authenticatedUserId = userId;
    }
    /**
     * Carrega os bilhetes do cliente logado.
     * @param clientId ID do cliente.
     */
    private void loadClientTickets(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            AlertUtils.showErrorAlert("Erro", "ID do cliente não encontrado.");
            return;
        }

        try {
            List<Ticket> tickets = fetchClientTickets(clientId);
            ticketsList.setAll(tickets);
            tableViewTickets.setItems(ticketsList);
        } catch (Exception e) {
            AlertUtils.showErrorAlert("Error","Não tens bilhetes!");
        }
    }
    /**
     * Fetch dos bilhetes do cliente na API.
     * @param clientId ID do cliente.
     * @return Lista de bilhetes.
     * @throws Exception Se ocorrer erro ao buscar os bilhetes.
     */
    private List<Ticket> fetchClientTickets(String clientId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(TICKET_URL + "client/" + clientId + "/"))
                .header("Accept", "application/json")
                .header("Authorization", authorizationHeader)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body().trim();
            JSONObject jsonResponse = new JSONObject(responseBody);

            if (!jsonResponse.has("TicketInfo")) {
                return Collections.emptyList();
            }

            JSONArray ticketsArray = jsonResponse.getJSONArray("TicketInfo");

            if (ticketsArray.isEmpty()) {
                return Collections.emptyList();
            }

            List<Ticket> tickets = new ArrayList<>();

            for (int i = 0; i < ticketsArray.length(); i++) {
                JSONObject ticketJson = ticketsArray.getJSONObject(i);

                String id = ticketJson.getString("Id");
                Date startDate = parseDate(ticketJson.getString("StartDate"));
                Date endDate = parseDate(ticketJson.getString("EndDate"));
                String location = ticketJson.getString("Location");
                String seat = ticketJson.getString("Seat");
                String qrCodeBase64 = ticketJson.optString("TicketQR", "");

                Ticket ticket = new Ticket(id, startDate, endDate, location, seat, qrCodeBase64);

                tickets.add(ticket);
            }

            return tickets;
        } else if (response.statusCode() == 406) {
            return Collections.emptyList();
        } else {
            throw new RuntimeException("Erro ao buscar tickets: Código " + response.statusCode());
        }
    }

    private List<Ticket> fetchGameTicket(List<Prova> provas) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;

        List<Ticket> allTickets = new ArrayList<>();

        for (Prova prova : provas) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(TICKET_URL + "game/" + prova.getId() + "/"))
                    .header("Accept", "application/json")
                    .header("Authorization", authorizationHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body().trim();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray ticketsArray = jsonResponse.getJSONArray("TicketInfo");

                for (int i = 0; i < ticketsArray.length(); i++) {
                    JSONObject ticketJson = ticketsArray.getJSONObject(i);

                    String id = ticketJson.getString("Id");
                    Date startDate = parseDate(ticketJson.getString("StartDate"));
                    Date endDate = parseDate(ticketJson.getString("EndDate"));
                    String location = ticketJson.getString("Location");
                    String seat = ticketJson.getString("Seat");
                    String qrCodeBase64 = ticketJson.optString("TicketQR", "");

                    Ticket ticket = new Ticket(id, startDate, endDate, location, seat, qrCodeBase64);
                    allTickets.add(ticket);
                }
            } else if (response.statusCode() == 406) {
            } else {
                throw new RuntimeException("Erro ao buscar tickets para o jogo " + prova.getId() + ": Código " + response.statusCode());
            }
        }

        return allTickets;
    }


    static void fetchGameTickets(List<Prova> provas) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String auth = USERNAME + ":" + PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authorizationHeader = "Basic " + encodedAuth;

        for (Prova prova : provas) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(TICKET_URL + "game/" + prova.getId() + "/"))
                    .header("Accept", "application/json")
                    .header("Authorization", authorizationHeader)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body().trim();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray ticketsArray = jsonResponse.getJSONArray("TicketInfo");

                int ticketsCount = ticketsArray.length();
                int updatedCapacity = Math.max(0, prova.getCapacity() - ticketsCount);
                prova.setCapacity(updatedCapacity);
            } else if (response.statusCode() == 406) {

            } else {
                throw new RuntimeException("Erro ao buscar tickets para o jogo " + prova.getId() + ": Código " + response.statusCode());
            }
        }
    }

    /**
     * Converte a string Base64 do QR Code numa imagem.
     * @param base64QrCode QR Code em formato Base64.
     * @return A imagem convertida.
     */
    private Image getQrCodeImageView(String base64QrCode) {
        if (base64QrCode.contains(",")) {
            base64QrCode = base64QrCode.substring(base64QrCode.indexOf(",") + 1);
        }

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64QrCode);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            return new Image(bis);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Exibe o QR Code num pop-up.
     * @param base64QrCode QR Code em formato Base64.
     */
    private void showQrCodePopup(String base64QrCode) {
        Image qrImage = getQrCodeImageView(base64QrCode);
        if (qrImage == null) {
            AlertUtils.showErrorAlert("Erro", "Não foi possível carregar o QR Code.");
            return;
        }

        ImageView qrImageView = new ImageView(qrImage);
        qrImageView.setFitWidth(250);
        qrImageView.setFitHeight(250);

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("QR Code");

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(qrImageView);

        Scene scene = new Scene(root, 300, 300);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
