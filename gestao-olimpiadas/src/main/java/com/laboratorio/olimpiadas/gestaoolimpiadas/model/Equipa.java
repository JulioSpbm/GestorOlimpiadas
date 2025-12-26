package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import java.util.List;

/**
 * Representa uma equipa participante nos Jogos Olímpicos, contendo informações como nome, país, género,
 * modalidade, ano de fundação e participações.
 */
public class Equipa {
    private String nome;
    private int pais;
    private boolean genero;
    private int idModalidade;
    private int anoFundacao;
    private Participacao participacao;
    private List<Participacao> participacoes;
    private String generoNome;
    private String modalidadeNome;
    private String paisNome;
    private int id;


    /**
     * Construtor para criar uma nova equipa.
     *
     * @param nome o nome da equipa.
     * @param pais o identificador do país da equipa.
     * @param genero o género da equipa (true para masculino, false para feminino).
     * @param idModalidade o identificador da modalidade da equipa.
     * @param anoFundacao o ano de fundação da equipa.
     * @param participacao a participação da equipa nos Jogos Olímpicos.
     */
    public Equipa(String nome, int pais, boolean genero, int idModalidade, int anoFundacao, Participacao participacao) {
        this.nome = nome;
        this.pais = pais;
        this.genero = genero;
        this.idModalidade = idModalidade;
        this.anoFundacao = anoFundacao;
        this.participacao = participacao;
    }

    public Equipa(){}

    /**
     * Obtém o nome da equipa.
     *
     * @return o nome da equipa.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da equipa.
     *
     * @param nome o nome a ser atribuído à equipa.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o identificador do país da equipa.
     *
     * @return o identificador do país da equipa.
     */
    public int getPais() {
        return pais;
    }

    /**
     * Define o identificador do país da equipa.
     *
     * @param pais o identificador do país a ser atribuído à equipa.
     */
    public void setPais(int pais) {
        this.pais = pais;
    }

    /**
     * Obtém o género da equipa.
     *
     * @return true se a equipa for masculina, false se for feminina.
     */
    public boolean getGenero() {
        return genero;
    }

    /**
     * Define o género da equipa.
     *
     * @param genero o género a ser atribuído à equipa (true para masculino, false para feminino).
     */
    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    /**
     * Obtém o identificador da modalidade da equipa.
     *
     * @return o identificador da modalidade da equipa.
     */
    public int getidModalidade() {
        return idModalidade;
    }

    /**
     * Define o identificador da modalidade da equipa.
     *
     * @param idModalidade o identificador da modalidade a ser atribuído à equipa.
     */
    public void setidModalidade(int idModalidade) {
        this.idModalidade = idModalidade;
    }

    /**
     * Obtém o ano de fundação da equipa.
     *
     * @return o ano de fundação da equipa.
     */
    public int getAnoFundacao() {
        return anoFundacao;
    }

    /**
     * Define o ano de fundação da equipa.
     *
     * @param anoFundacao o ano de fundação a ser atribuído à equipa.
     */
    public void setAnoFundacao(int anoFundacao) {
        this.anoFundacao = anoFundacao;
    }

    /**
     * Obtém a participação da equipa nos Jogos Olímpicos.
     *
     * @return a participação da equipa.
     */
    public Participacao getParticipacao() {
        return participacao;
    }

    /**
     * Define a participação da equipa nos Jogos Olímpicos.
     *
     * @param participacao a participação a ser atribuída à equipa.
     */
    public void setParticipacao(Participacao participacao) {
        this.participacao = participacao;
    }

    public List<Participacao> getParticipacoes() {
        return participacoes;
    }

    public void setParticipacoes(List<Participacao> participacoes) {
        this.participacoes = participacoes;
    }

    /**
     * Obtém o pais da equipa.
     *
     * @return o pais da equipa.
     */
    public String getPaisNome() {
        return paisNome;
    }

    /**
     * Define o pais da equipa.
     *
     * @param paisNome o pais a ser atribuído à equipa.
     */
    public void setPaisNome(String paisNome) {
        this.paisNome = paisNome;
    }

    /**
     * Obtém a modalidade da equipa.
     *
     * @return a modalidade da equipa.
     */
    public String getModalidadeNome() {
        return modalidadeNome;
    }

    /**
     * Define a modalidade da equipa.
     *
     * @param modalidadeNome a modalidade a ser atribuído à equipa.
     */
    public void setModalidadeNome(String modalidadeNome) {
        this.modalidadeNome = modalidadeNome;
    }

    /**
     * Obtém o genero da equipa.
     *
     * @return o genero da equipa.
     */
    public String getGeneroNome() {
        return generoNome;
    }

    /**
     * Define o genero da equipa.
     *
     * @param generoNome o genero a ser atribuído à equipa.
     */
    public void setGeneroNome(String generoNome) {
        this.generoNome = generoNome;
    }

    public int getId() {return id;}
    public void setId(int id) { this.id = id;}
}
