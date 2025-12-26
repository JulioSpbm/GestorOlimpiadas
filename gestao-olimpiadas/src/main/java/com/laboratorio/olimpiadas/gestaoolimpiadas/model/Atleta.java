package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Classe que representa um Atleta, com informações pessoais e de participação nas Olimpíadas.
 */
public class Atleta {
    private String nome;
    private int idPais;
    private Date dataNascimento;
    private boolean genero;
    private int altura;
    private int peso;
    private Participacao participacao;
    private List<Participacao> participacoes;
    private Utilizador utilizador;
    private String generoNome;
    private String paisNome;
    private int id;
    private String estado;
    private boolean estadoBool;

    /**
     * Construtor da classe Atleta.
     *
     * @param nome           Nome do atleta.
     * @param idPais         ID do país do atleta.
     * @param genero         Género do atleta (true para masculino, false para feminino).
     * @param altura         Altura do atleta em centímetros.
     * @param peso           Peso do atleta em quilogramas.
     * @param dataNascimento Data de nascimento do atleta.
     * @param participacao   Objecto Participacao representando a participação do atleta.
     * @param utilizador     Objecto Utilizador associado ao atleta.
     */
    public Atleta(int id, String nome, int idPais, boolean genero, int altura, int peso, Date dataNascimento, Participacao participacao, Utilizador utilizador) {

    }

    public Atleta(String nome, int idPais, boolean genero, int altura, int peso, Date dataNascimento, Participacao participacao, Utilizador utilizador, String estadoAtleta) {
        this.nome = nome;
        this.idPais = idPais;
        this.genero = genero;
        this.altura = altura;
        this.peso = peso;
        this.dataNascimento = dataNascimento;
        this.participacao = participacao;
        this.utilizador = utilizador;
        this.estado = estadoAtleta;
    }

    public Atleta(){}

    public Atleta(String nome, int idPais, boolean generoo, int alturaa, int pesoo, java.sql.Date dataNascimentoo, Participacao participacao, Utilizador utilizador) {
        this.nome = nome;
        this.idPais = idPais;
        this.genero = genero;
        this.altura = altura;
        this.peso = peso;
        this.dataNascimento = dataNascimento;
        this.participacao = participacao;
        this.utilizador = utilizador;
    }

    public Atleta(Integer id, String nome, int idPais, boolean generoo, String altura, int pesoo, java.sql.Date dataNascimentoo, Participacao participacao, Utilizador utilizador, String estadoAtleta) {
        this.id = id;
        this.nome = nome;
        this.idPais = idPais;
        this.genero = generoo;
        this.altura = Integer.parseInt(altura);
        this.peso = pesoo;
        this.dataNascimento = dataNascimentoo;
        this.participacao = participacao;
        this.utilizador = utilizador;
        this.estado = estadoAtleta;
    }


    /**
     * Obtém o nome do atleta.
     * @return Nome do atleta.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do atleta.
     * @param nome Nome do atleta.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o ID do país do atleta.
     * @return ID do país do atleta.
     */
    public int getidPais() {
        return idPais;
    }

    /**
     * Define o ID do país do atleta.
     * @param idPais ID do país do atleta.
     */
    public void setidPais(int idPais) {
        this.idPais = idPais;
    }

    /**
     * Obtém o género do atleta.
     * @return true se for masculino, false se for feminino.
     */
    public boolean getGenero() {
        return genero;
    }

    /**
     * Define o género do atleta.
     * @param genero true para masculino, false para feminino.
     */
    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    /**
     * Obtém a altura do atleta em centímetros.
     * @return Altura do atleta.
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Define a altura do atleta.
     * @param altura Altura do atleta em centímetros.
     */
    public void setAltura(int altura) {
        this.altura = altura;
    }

    /**
     * Obtém o peso do atleta em quilogramas.
     * @return Peso do atleta.
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Define o peso do atleta.
     * @param peso Peso do atleta em quilogramas.
     */
    public void setPeso(int peso) {
        this.peso = peso;
    }

    /**
     * Obtém a data de nascimento do atleta no formato "yyyy-MM-dd".
     * @return Data de nascimento do atleta como String.
     */
    public String getDataNascimento() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dataNascimento);
    }

    /**
     * Define a data de nascimento do atleta.
     * @param dataNascimento Data de nascimento do atleta.
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * Obtém a participação do atleta nas Olimpíadas.
     * @return Objecto Participacao do atleta.
     */
    public Participacao getParticipacao() {
        return participacao;
    }

    /**
     * Define a participação do atleta nas Olimpíadas.
     * @param participacao Objecto Participacao do atleta.
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
     * Obtém o utilizador associado ao atleta.
     * @return Objecto Utilizador do atleta.
     */
    public Utilizador getUtilizador() {
        return utilizador;
    }

    /**
     * Define o utilizador associado ao atleta.
     * @param utilizador Objecto Utilizador do atleta.
     */
    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    /**
     * Obtém o pais do atleta.
     * @return pais do atleta.
     */
    public String getPaisNome() {
        return paisNome;
    }

    /**
     * Define o pais do atleta.
     * @param paisNome pais do atleta.
     */
    public void setPaisNome(String paisNome) {
        this.paisNome = paisNome;
    }

    /**
     * Obtém o genero do atleta.
     * @return genero do atleta.
     */
    public String getGeneroNome() {
        return generoNome;
    }

    /**
     * Define o genero do atleta.
     * @param generoNome genero do atleta.
     */
    public void setGeneroNome(String generoNome) {
        this.generoNome = generoNome;
    }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Atleta {" +
                "Nome: '" + nome + '\'' +
                ", País: " + idPais +
                ", Género: " + (genero ? "Masculino" : "Feminino") +
                ", Altura: " + altura + " cm" +
                ", Peso: " + peso + " kg" +
                ", Participação: " + participacao +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData(int ano) {
    }


    private List<Integer> anosParticipacao; // Lista para armazenar os anos

    public List<Integer> getAnosParticipacao() {
        return anosParticipacao;
    }

    public void setAnosParticipacao(List<Integer> anosParticipacao) {
        this.anosParticipacao = anosParticipacao;
    }
}
