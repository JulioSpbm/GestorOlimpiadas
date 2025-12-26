package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import com.laboratorio.olimpiadas.gestaoolimpiadas.service.UtilizadorService;

import java.sql.SQLException;

public class XML {
    private String data;
    private int id_utilizador;
    private String ficheiro;
    private String utilizadorNome;

    // Construtor
    public XML(String data, int id_utilizador, String ficheiro) {
        this.data = data;
        this.id_utilizador = id_utilizador;
        this.ficheiro = ficheiro;
    }

    public XML(){}

    // Getters
    public String getData() {
        return data;
    }

    public int getId_utilizador() {
        return id_utilizador;
    }

    public String getFicheiro() {
        return ficheiro;
    }

    // Setters
    public void setData(String data) {
        this.data = data;
    }

    public void setId_utilizador(int id_utilizador) throws SQLException {
        this.id_utilizador = id_utilizador;
    }

    public void setFicheiro(String ficheiro) {
        this.ficheiro = ficheiro;
    }


    public String getUtilizadorNome() {
        return utilizadorNome;
    }

    public void setUtilizadorNome(String utilizadorNome) {
        this.utilizadorNome = utilizadorNome;
    }
}
