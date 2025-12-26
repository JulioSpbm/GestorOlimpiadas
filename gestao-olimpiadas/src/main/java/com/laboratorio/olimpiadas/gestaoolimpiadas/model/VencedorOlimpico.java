package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import com.laboratorio.olimpiadas.gestaoolimpiadas.controller.ModalidadeController;

public class VencedorOlimpico {
    private int id_medalha;
    private float resultado;
    private int ano_vencedor;

    private String utilizadorNome;
    private String equipaNome;
    private String medalhaNome;
    private String resultadoNome;

    public VencedorOlimpico(String utilizadorNome, String equipaNome, int id_medalha, float resultado, int ano_vencedor) {

        this.utilizadorNome = utilizadorNome;
        this.equipaNome = equipaNome;
        this.id_medalha = id_medalha;
        this.resultado = resultado;
        this.ano_vencedor = ano_vencedor;
    }

    public VencedorOlimpico(){}

    public int getId_medalha() {
        return id_medalha;
    }

    public void setId_medalha(int id_medalha) {
        this.id_medalha = id_medalha;
    }

    public float getResultado() {
        return resultado;
    }

    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    public int getAno_vencedor() {
        return ano_vencedor;
    }

    public void setAno_vencedor(int ano_vencedor) {
        this.ano_vencedor = ano_vencedor;
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

    public String getMedalhaNome() {
        return medalhaNome;
    }

    public void setMedalhaNome(String medalhaNome) {
        this.medalhaNome = medalhaNome;
    }

    public String getResultadoNome() {
        return resultadoNome;
    }

    public void setResultadoNome(String resultadoNome) {
        this.resultadoNome = resultadoNome;
    }
}
