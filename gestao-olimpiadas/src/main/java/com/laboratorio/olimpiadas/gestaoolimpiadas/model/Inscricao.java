package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Inscricao {
    private int id;
    private StringProperty utilizador;
    private StringProperty equipa;
    private StringProperty modalidade;
    private StringProperty evento;
    private StringProperty estado;

    public Inscricao(int id, String utilizador, String equipa, String modalidade, String evento, String estado) {
        this.id = id;
        this.utilizador = new SimpleStringProperty(utilizador);
        this.equipa = new SimpleStringProperty(equipa);
        this.modalidade = new SimpleStringProperty(modalidade);
        this.evento = new SimpleStringProperty(evento);
        this.estado = new SimpleStringProperty(estado);
    }


    public Inscricao(String estado) {
        this.estado = new SimpleStringProperty(estado);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUtilizador() {
        return utilizador.get();
    }

    public void setUtilizador(String utilizador) {
        this.utilizador.set(utilizador);
    }

    public StringProperty utilizadorProperty() {
        return utilizador;
    }

    // Getters e Setters para Equipa
    public String getEquipa() {
        return equipa.get();
    }

    public void setEquipa(String equipa) {
        this.equipa.set(equipa);
    }

    public StringProperty equipaProperty() {
        return equipa;
    }

    // Getters e Setters para Modalidade
    public String getModalidade() {
        return modalidade.get();
    }

    public void setModalidade(String modalidade) {
        this.modalidade.set(modalidade);
    }

    public StringProperty modalidadeProperty() {
        return modalidade;
    }

    // Getters e Setters para Evento
    public String getEvento() {
        return evento.get();
    }

    public void setEvento(String evento) {
        this.evento.set(evento);
    }

    public StringProperty eventoProperty() {
        return evento;
    }

    // Getters e Setters para Estado
    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }
}
