package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import java.util.List;

public class Resultado {

    private String nome;
    private String genero;
    private String tipo;
    private String pontuacao;
    private int numeroInscricoes;
    private List<String> ids;
    private double valorResultadoIndividual;
    private double valorResultadoTeam;
    private int idModalidade;
    private int idAtleta;
    private int idEquipa;
    private int idMedalha;
    private String dataResultado;
    private int idEvento;

    private String nomeModalidade;
    private String nomeAtleta;
    private String nomeEquipa;
    private String nomeMedalha;
    private String nomeEvento;

    // Construtor
    public Resultado(String nome, String genero, String tipo, String pontuacao, int numeroInscricoes,List<String>  ids) {
        this.nome = nome;
        this.genero = genero;
        this.tipo = tipo;
        this.pontuacao = pontuacao;
        this.numeroInscricoes = numeroInscricoes;
        this.ids = ids;
    }

    public Resultado(){};

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(String pontuacao) {
        this.pontuacao = pontuacao;
    }

    public int getNumeroInscricoes() {
        return numeroInscricoes;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public void setNumeroInscricoes(int numeroInscricoes) {
        this.numeroInscricoes = numeroInscricoes;
    }

    public double getPontuacaoIndividual() {
        return valorResultadoIndividual;
    }

    public void setPontuacaoIndividual(double valorResultadoIndividual) {
        this.valorResultadoIndividual = valorResultadoIndividual;
    }

    public double getPontuacaoTeam() {
        return valorResultadoTeam;
    }

    public void setPontuacaoTeam(double valorResultadoTeam) {
        this.valorResultadoTeam = valorResultadoTeam;
    }

    public int getIdModalidade() {
        return idModalidade;
    }

    public void setIdModalidade(int idModalidade) {
        this.idModalidade = idModalidade;
    }

    public int getIdAtleta() {
        return idAtleta;
    }

    public void setIdAtleta(int idAtleta) {
        this.idAtleta = idAtleta;
    }

    public int getIdEquipa() {
        return idEquipa;
    }

    public void setIdEquipa(int idEquipa) {
        this.idEquipa = idEquipa;
    }

    public int getIdMedalha() {
        return idMedalha;
    }

    public void setIdMedalha(int idMedalha) {
        this.idMedalha = idMedalha;
    }

    public String getDataResultado() {
        return dataResultado;
    }

    public void setDataResultado(String dataResultado) {
        this.dataResultado = dataResultado;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNomeModalidade() {
        return nomeModalidade;
    }

    public void setNomeModalidade(String nomeModalidade) {
        this.nomeModalidade = nomeModalidade;
    }

    public String getNomeAtleta() {
        return nomeAtleta;
    }

    public void setNomeAtleta(String nomeAtleta) {
        this.nomeAtleta = nomeAtleta;
    }

    public String getNomeEquipa() {
        return nomeEquipa;
    }

    public void setNomeEquipa(String nomeEquipa) {
        this.nomeEquipa = nomeEquipa;
    }

    public String getNomeMedalha() {
        return nomeMedalha;
    }

    public void setNomeMedalha(String nomeMedalha) {
        this.nomeMedalha = nomeMedalha;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }
}
