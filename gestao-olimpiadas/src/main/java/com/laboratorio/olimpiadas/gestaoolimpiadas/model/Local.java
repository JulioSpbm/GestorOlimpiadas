package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

/**
 * Representa um local onde eventos dos Jogos Olímpicos podem ocorrer.
 */
public class Local {
    private int id;
    private String nome;
    private String morada;
    private String cidade;
    private int capacidade;
    private int anoConstrucao;
    private boolean tipoLocal;
    private String tipoLocalNome;

    /**
     * Construtor para editar uma instância de Local.
     * @param id ID do local.
     * @param nome Nome do local.
     * @param morada Endereço do local.
     * @param cidade Cidade onde o local se situa.
     * @param capacidade Capacidade máxima de pessoas que o local suporta.
     * @param anoConstrucao Ano de construção do local.
     */

    public Local(int id, String nome, String morada, String cidade, int capacidade, int anoConstrucao, boolean tipoLocal) {
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.anoConstrucao = anoConstrucao;
        this.tipoLocal = tipoLocal;
    }

    /**
     * Construtor para criar uma nova instância de Local.
     * @param nome          Nome do local.
     * @param morada        Endereço do local.
     * @param cidade        Cidade onde o local se situa.
     * @param capacidade    Capacidade máxima de pessoas que o local suporta.
     * @param anoConstrucao Ano de construção do local.
     */
    public Local(String nome, String morada, String cidade, int capacidade, int anoConstrucao, boolean tipoLocal) {
        this.nome = nome;
        this.morada = morada;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.anoConstrucao = anoConstrucao;
        this.tipoLocal = tipoLocal;
    }

    public Local(String nome, String morada, String cidade, int capacidadee, int anoConstrucaoo, String tipoLocall) {
    }

    public Local(int id, String nome, String morada, String cidade, int capacidadee, int anoConstrucaoo, String tipoLocall) {
        this.id = id;
        this.nome = nome;
        this.morada = morada;
        this.cidade = cidade;
        this.capacidade = capacidadee;
        this.anoConstrucao = anoConstrucaoo;
        this.tipoLocalNome = tipoLocall;
    }

    public Local(){

    }

    /**
     * Obtém o ID do local.
     * @return ID do local.
     */

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Obtém o nome do local.
     * @return Nome do local.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do local.
     * @param nome Nome do local.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a morada do local.
     * @return Morada do local.
     */
    public String getMorada() {
        return morada;
    }

    /**
     * Define a morada do local.
     * @param morada Morada do local.
     */
    public void setMorada(String morada) {this.morada = morada;}

    /**
     * Obtém a cidade onde o local se situa.
     * @return Cidade do local.
     */
    public String getCidade() {return cidade;}

    /**
     * Define a cidade onde o local se situa.
     * @param cidade Cidade do local.
     */
    public void setCidade(String cidade) {this.cidade = cidade;}

    /**
     * Obtém a capacidade máxima de pessoas que o local suporta.
     *
     * @return Capacidade do local.
     */
    public int getCapacidade() {
        return capacidade;
    }

    /**
     * Define a capacidade máxima de pessoas que o local suporta.
     * @param capacidade Capacidade do local.
     */
    public void setCapacidade(int capacidade) {this.capacidade = capacidade;}

    /**
     * Obtém o ano de construção do local.
     *
     * @return Ano de construção do local.
     */
    public int getAnoConstrucao() {
        return anoConstrucao;
    }

    /**
     * Define o ano de construção do local.
     * @param anoConstrucao Ano de construção do local.
     */
    public void setAnoConstrucao(int anoConstrucao) {this.anoConstrucao = anoConstrucao;}


    public void setTipoLocal(boolean tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public boolean getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocalNome(String tipoLocalNome){
        this.tipoLocalNome = tipoLocalNome;
    }

    public String getTipoLocalNome(){
        return tipoLocalNome;
    }
}
