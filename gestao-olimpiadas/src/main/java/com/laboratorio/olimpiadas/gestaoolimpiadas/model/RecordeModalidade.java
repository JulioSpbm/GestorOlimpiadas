package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

public class RecordeModalidade {
    private int num_medalhas;
    private float record_olimpico;
    private int ano_recorde;

    private String utilizadorNome;
    private String equipaNome;
    private String num_medalhasNome;
    private String record_olimpicoNome;


    public RecordeModalidade(String utilizadorNome, String equipaNome, int num_medalhas, float record_olimpico, int ano_recorde) {
        this.utilizadorNome = utilizadorNome;
        this.equipaNome = equipaNome;
        this.num_medalhas = num_medalhas;
        this.record_olimpico = record_olimpico;
        this.ano_recorde = ano_recorde;
    }

    public RecordeModalidade(){
    }

    public RecordeModalidade(float recordeOlimpico) {
    }

    public int getNum_medalhas() {
        return num_medalhas;
    }

    public void setNum_medalhas(int num_medalhas) {
        this.num_medalhas = num_medalhas;
    }

    public float getRecord_olimpico() {
        return record_olimpico;
    }

    public void setRecord_olimpico(float record_olimpico) {
        this.record_olimpico = record_olimpico;
    }

    public int getAno_recorde() {
        return ano_recorde;
    }

    public void setAno_recorde(int ano_recorde) {
        this.ano_recorde = ano_recorde;
    }

    public String getUtilizadorNome() {
        return utilizadorNome;
    }

    public void setUtilizadorNome(String utilizadorNome) {
        this.utilizadorNome = utilizadorNome;
    }

    public String getEquipaNome() {
        return equipaNome;
    }

    public void setEquipaNome(String equipaNome) {
        this.equipaNome = equipaNome;
    }

    public String getNum_medalhasNome() {
        return num_medalhasNome;
    }

    public void setNum_medalhasNome(String num_medalhasNome) {
        this.num_medalhasNome = num_medalhasNome;
    }

    public String getRecord_olimpicoNome() {
        return record_olimpicoNome;
    }

    public void setRecord_olimpicoNome(String record_olimpicoNome) {
        this.record_olimpicoNome = record_olimpicoNome;
    }
}
