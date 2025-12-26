package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.ModalidadeRecorde;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class RecordModalityController {

    @FXML
    private TableView<ModalidadeRecorde> tableViewRecordes;
    @FXML
    private TableColumn<ModalidadeRecorde, String> colModalidade;
    @FXML
    private TableColumn<ModalidadeRecorde, Float> colRecorde;
    @FXML
    private TableColumn<ModalidadeRecorde, Integer> colAno;

    public void setModalidades(List<ModalidadeRecorde> records) {
        ObservableList<ModalidadeRecorde> list = FXCollections.observableArrayList(records);
        tableViewRecordes.setItems(list);
    }

    @FXML
    public void initialize() {
        colModalidade.setCellValueFactory(new PropertyValueFactory<>("modalidade"));
        colRecorde.setCellValueFactory(new PropertyValueFactory<>("recorde"));
        colAno.setCellValueFactory(new PropertyValueFactory<>("ano"));
    }
}
