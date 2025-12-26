package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Evento;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Modalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Partida;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.PartidaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;

public class PartidaController {
    @FXML
    public TableView<Partida> tableViewMatch;
    @FXML
    public TableColumn<Partida, String> colMatEvent;
    @FXML
    public TableColumn<Partida, String> colMatLocal;
    @FXML
    public TableColumn<Partida, String> colMatModalidade;
    @FXML
    public TableColumn<Partida, Timestamp> colMatData;
    @FXML
    public Button btnDeleteMatch;

    private PartidaService partidaService = new PartidaService();

    @FXML
    public void initialize() throws SQLException {
        colMatEvent.setCellValueFactory(new PropertyValueFactory<>("eventoNome"));
        colMatLocal.setCellValueFactory(new PropertyValueFactory<>("localNome"));
        colMatModalidade.setCellValueFactory(new PropertyValueFactory<>("modalidadeNome"));
        colMatData.setCellValueFactory(new PropertyValueFactory<>("data_hora"));
        carregarDados();

        btnDeleteMatch.setOnAction(event -> {
            Partida selectedPartida = tableViewMatch.getSelectionModel().getSelectedItem();
            if (selectedPartida != null) {
                deleteMatch(selectedPartida);
            } else {
                AlertUtils.showErrorAlert("Error","Select a match!");
            }
        });
    }

    private void deleteMatch(Partida selectedPartida) {
        partidaService.deleteMatch(selectedPartida);
    }

    public void carregarDados() throws SQLException {
        if (tableViewMatch != null) {
            ObservableList<Partida> partidas = FXCollections.observableArrayList(partidaService.getAllMatches());
            tableViewMatch.setItems(partidas);
        }
    }
}
