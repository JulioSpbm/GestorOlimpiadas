package com.laboratorio.olimpiadas.gestaoolimpiadas.controller;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Inscricao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.InscricaoService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.LocalService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.util.List;

public class InscricaoController {

    private TableView<Inscricao> tableViewInscricoes;
    private InscricaoService inscricaoService;

    public InscricaoController() {
        this.inscricaoService = new InscricaoService();
    }

    public void inicializarTabela(TableView<Inscricao> tableViewInscricoes) {
        this.tableViewInscricoes = tableViewInscricoes;
    }

    public List<Inscricao> obterInscricoes() {
        return inscricaoService.listarInscricoes();
    }

    /**
     * Carrega os dados de inscrições e exibe os na tabela.
     */
    public void carregarDados() {
        if (tableViewInscricoes != null) {
            ObservableList<Inscricao> inscricoes = FXCollections.observableArrayList(inscricaoService.listarInscricoes());
            tableViewInscricoes.setItems(inscricoes);
        }
    }
}
