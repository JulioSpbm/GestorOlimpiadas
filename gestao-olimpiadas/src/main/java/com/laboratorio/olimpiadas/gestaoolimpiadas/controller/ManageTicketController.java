package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManageTicketController {

    @FXML
    private TableView<Ticket> tableViewClientes;

    @FXML
    private TableColumn<Ticket, String> colStartDateTicket;

    @FXML
    private TableColumn<Ticket, String> colEndDateTicket;

    @FXML
    private TableColumn<Ticket, String> colLocationTicket;

    @FXML
    private TableColumn<Ticket, String> colSeatTicket;

    private final ObservableList<Ticket> tickets = FXCollections.observableArrayList();
    private static final String USERNAME = "EG3";
    private static final String PASSWORD = "7?bo?PL03S";

    private String clientId; // Para armazenar o ID do cliente atual

    public void initialize() {
        colStartDateTicket.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDateTicket.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colLocationTicket.setCellValueFactory(new PropertyValueFactory<>("location"));
        colSeatTicket.setCellValueFactory(new PropertyValueFactory<>("seat"));
        carregarTickets();
    }

    public void setClientId(String clientId) {
        if (clientId == null || clientId.isEmpty()) {
            System.err.println("Erro: clientId está nulo ou vazio!");
            return;
        }
        this.clientId = clientId;
        carregarTickets();
    }

    private void carregarTickets() {
        if (clientId == null || clientId.isEmpty()) {
            System.err.println("Erro: clientId está nulo ou vazio!");
            return;
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

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                List<Ticket> ticketList = parseTickets(responseBody);
                tickets.setAll(ticketList);
                tableViewClientes.setItems(tickets);
            } else {
                System.err.println("Erro ao buscar os tickets: Código " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar tickets: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Ticket> parseTickets(String responseBody) {
        List<Ticket> tickets = new ArrayList<>();

        try {
            // Limpar o JSON para extrair apenas o array de tickets
            String ticketInfoJson = responseBody
                    .replaceFirst("\\{\"Status\":\"OK\",\"TicketInfo\":", "")
                    .replaceFirst("\\}$", "");

            String[] jsonTickets = ticketInfoJson.split("},\\{");

            DateTimeFormatter[] dateFormats = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH")
            };

            for (String jsonTicket : jsonTickets) {
                jsonTicket = jsonTicket.replaceAll("[\\[\\]{}\"]", "");
                String[] keyValuePairs = jsonTicket.split(",");
                Ticket ticket = new Ticket();

                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length < 2) continue;
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "Id":
                            ticket.setId(value);
                            break;
                        case "StartDate":
                            ticket.setStartDate(parseDate(value, dateFormats));
                            break;
                        case "EndDate":
                            ticket.setEndDate(parseDate(value, dateFormats));
                            break;
                        case "Location":
                            ticket.setLocation(value);
                            break;
                        case "Seat":
                            ticket.setSeat(value);
                            break;
                        default:
                            break;
                    }
                }
                tickets.add(ticket);
            }
        } catch (Exception e) {
            System.err.println("Erro ao fazer o parse do JSON: " + e.getMessage());
        }

        return tickets;
    }

    private static Timestamp parseDate(String dateString, DateTimeFormatter[] formats) {
        for (DateTimeFormatter format : formats) {
            try {
                return Timestamp.valueOf(LocalDateTime.parse(dateString, format));
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("Formato de data inválido: " + dateString);
    }
}
