package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import java.sql.Timestamp;

public class Partida {
    private int id_local;
    private int id_evento;
    private int id_modalidade;
    private Timestamp data_hora;

    public Partida(int id_local, int id_evento, int id_modalidade, Timestamp data_hora) {
        this.id_local = id_local;
        this.id_evento = id_evento;
        this.id_modalidade = id_modalidade;
        this.data_hora = data_hora;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public int getId_modalidade() {
        return id_modalidade;
    }

    public void setId_modalidade(int id_modalidade) {
        this.id_modalidade = id_modalidade;
    }

    public Timestamp getData_hora() {
        return data_hora;
    }

    public void setData_hora(Timestamp data_hora) {
        this.data_hora = data_hora;
    }

    private String localNome;
    private String eventoNome;
    private String modalidadeNome;

    public String getLocalNome() {
        return localNome;
    }

    public void setLocalNome(String localNome) {
        this.localNome = localNome;
    }

    public String getEventoNome() {
        return eventoNome;
    }

    public void setEventoNome(String eventoNome) {
        this.eventoNome = eventoNome;
    }

    public String getModalidadeNome() {
        return modalidadeNome;
    }

    public void setModalidadeNome(String modalidadeNome) {
        this.modalidadeNome = modalidadeNome;
    }
}
