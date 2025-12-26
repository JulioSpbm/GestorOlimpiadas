package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.EquipaRecorde;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RecordTeamController {
    @FXML
    private TableView<EquipaRecorde> tableViewRecordes;
    @FXML
    private TableColumn<EquipaRecorde, String> colEquipa;
    @FXML
    private TableColumn<EquipaRecorde, Float> colRecordeEquipa;
    @FXML
    private TableColumn<EquipaRecorde, Integer> colAnoEquipa;

    public void setEquipas(List<EquipaRecorde> records){
        ObservableList<EquipaRecorde> list = FXCollections.observableArrayList(records);
        tableViewRecordes.setItems(list);
    }

    @FXML
    public void initialize() {
        colEquipa.setCellValueFactory(new PropertyValueFactory<>("equipa"));
        colRecordeEquipa.setCellValueFactory(new PropertyValueFactory<>("recorde"));
        colAnoEquipa.setCellValueFactory(new PropertyValueFactory<>("ano"));
    }
}
