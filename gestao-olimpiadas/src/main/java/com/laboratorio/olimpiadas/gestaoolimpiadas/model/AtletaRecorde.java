package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

public class AtletaRecorde {
    private String atleta;
    private float recorde;
    private int ano;
    private String nome_modalidade;
    private int num_medalhas;

    public AtletaRecorde() {
    }

    public AtletaRecorde(String nome_modalidade, float recorde, int ano, int num_medalhas) {
        this.nome_modalidade = nome_modalidade;
        this.recorde = recorde;
        this.ano = ano;
        this.num_medalhas = num_medalhas;
    }

    public String getAtleta() {
        return atleta;
    }

    public void setAtleta(String atleta) {
        this.atleta = atleta;
    }

    public float getRecorde() {
        return recorde;
    }

    public void setRecorde(float recorde) {
        this.recorde = recorde;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getNome_modalidade() {
        return nome_modalidade;
    }

    public void setNome_modalidade(String nome_modalidade) {
        this.nome_modalidade = nome_modalidade;
    }

    public int getNum_medalhas() {
        return num_medalhas;
    }

    public void setNum_medalhas(int num_medalhas) {
        this.num_medalhas = num_medalhas;
    }
}
