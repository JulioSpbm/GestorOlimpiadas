package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.AtletaRecorde;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RecordAthleteController {
    @FXML
    private TableView<AtletaRecorde> tableViewRecordes;
    @FXML
    private TableColumn<AtletaRecorde, String> colAthlete;
    @FXML
    private TableColumn<AtletaRecorde, Float> colRecordeAthlete;
    @FXML
    private TableColumn<AtletaRecorde, Integer> colAnoAthlete;

    public void setAtletas(List<AtletaRecorde> records){
        ObservableList<AtletaRecorde> list = FXCollections.observableArrayList(records);
        tableViewRecordes.setItems(list);
    }

    @FXML
    public void initialize() {
        colAthlete.setCellValueFactory(new PropertyValueFactory<>("atleta"));
        colRecordeAthlete.setCellValueFactory(new PropertyValueFactory<>("recorde"));
        colAnoAthlete.setCellValueFactory(new PropertyValueFactory<>("ano"));
    }
}
