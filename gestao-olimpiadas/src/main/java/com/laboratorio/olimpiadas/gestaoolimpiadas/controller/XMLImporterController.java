package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.XML;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.AtletaService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.XMLImporterService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class XMLImporterController {

    @FXML
    public TableView<XML> tableViewHist;
    @FXML
    public TableColumn<XML,String> colHistData;
    @FXML
    public TableColumn<XML,String> colHistUtilizador;
    @FXML
    public TableColumn<XML,String> colHistFicheiro;

    private final XMLImporterService xmlService;

    public XMLImporterController() {
        this.xmlService = new XMLImporterService();
    }

    @FXML
    public void initialize() {
    }

    /**
     *
     * @param tableViewHist
     */
    public void inicializarTabela(TableView<XML> tableViewHist) {
        this.tableViewHist = tableViewHist;

    }

    /**
     * Carrega os dados na tabela {@code tableViewHist} a partir da lista de objetos {@code XML} retornada
     */
    public void carregarDados() {
        if (tableViewHist != null) {
            ObservableList<XML> data = FXCollections.observableArrayList(xmlService.listarHist());
            tableViewHist.setItems(data);
        }
    }
}
