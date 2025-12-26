package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import javafx.scene.image.Image;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Representa um evento relacionado a uma Olimpíada, incluindo informações sobre a mascote,
 * a descrição, e as datas de início e fim do evento.
 */
public class Evento {
    private int id;
    private Olimpiada olimpiada;
    private String nome;
    private String ano;
    private String nome_mascote;
    private String descricao_mascote;
    private String foto_mascote;
    private Image imageFileMascote;
    private Date data_inicio;
    private Date data_fim;
    private String nomePais;
    private int idMascote;

    /**
     * Constrói um novo evento com as informações fornecidas.
     *
     * @param olimpiada A Olimpíada associada ao evento.
     * @param nome O nome do evento.
     * @param nome_mascote O nome da mascote do evento.
     * @param descricao_mascote A descrição da mascote do evento.
     * @param foto_mascote O caminho da foto da mascote do evento.
     * @param data_inicio A data de início do evento.
     * @param data_fim A data de fim do evento.
     */

    public Evento(Olimpiada olimpiada, String nome, String ano, String nome_mascote, String descricao_mascote, String foto_mascote, Date data_inicio, Date data_fim) {
        this.olimpiada = olimpiada;
        this.nome = nome;
        this.ano = ano;
        this.nome_mascote = nome_mascote;
        this.descricao_mascote = descricao_mascote;
        this.foto_mascote = foto_mascote;
        this.data_inicio = data_inicio;
        this.data_fim = data_fim;
    }


    public Evento() {}

    /**
     * Retorna a Olimpíada associada ao evento.
     *
     * @return A Olimpíada associada ao evento.
     */
    public Olimpiada getOlimpiada() {
        return olimpiada;
    }

    /**
     * Define a Olimpíada associada ao evento.
     *
     * @param olimpiada A Olimpíada a ser associada ao evento.
     */
    public void setOlimpiada(Olimpiada olimpiada) {
        this.olimpiada = olimpiada;
    }

    /**
     * Retorna o nome do evento.
     *
     * @return O nome do evento.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do evento.
     *
     * @param nome O nome do evento.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }


    /**
     * Retorna o ano do evento.
     *
     * @return O ano do evento.
     */
    public String getAno() {
        return ano;
    }

    /**
     * Define o ano do evento.
     *
     * @param ano O ano do evento.
     */
    public void setAno(String ano) {
        this.ano = ano;
    }

    /**
     * Retorna o nome da mascote do evento.
     *
     * @return O nome da mascote do evento.
     */
    public String getNome_mascote() {
        return nome_mascote;
    }

    /**
     * Define o nome da mascote do evento.
     *
     * @param nome_mascote O nome da mascote do evento.
     */
    public void setNome_mascote(String nome_mascote) {
        this.nome_mascote = nome_mascote;
    }

    /**
     * Retorna a descrição da mascote do evento.
     *
     * @return A descrição da mascote do evento.
     */
    public String getDescricao_mascote() {
        return descricao_mascote;
    }

    /**
     * Define a descrição da mascote do evento.
     *
     * @param descricao_mascote A descrição da mascote do evento.
     */
    public void setDescricao_mascote(String descricao_mascote) {
        this.descricao_mascote = descricao_mascote;
    }

    /**
     * Retorna a data de início do evento formatada como "yyyy-MM-dd".
     *
     * @return A data de início do evento no formato "yyyy-MM-dd".
     */
    public String getData_inicio() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data_inicio);
    }

    /**
     * Define a data de início do evento.
     *
     * @param data_inicio A data de início do evento.
     */
    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    /**
     * Retorna a data de fim do evento formatada como "yyyy-MM-dd".
     *
     * @return A data de fim do evento no formato "yyyy-MM-dd".
     */
    public String getData_fim() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data_fim);
    }

    /**
     * Define a data de fim do evento.
     *
     * @param data_fim A data de fim do evento.
     */
    public void setData_fim(Date data_fim) {
        this.data_fim = data_fim;
    }

    /**
     * Retorna o pais do evento.
     *
     * @return O pais do evento.
     */
    public String getNomePais() { return nomePais; }
    /**
     * Define o pais do evento.
     *
     * @param nomePais O pais do evento.
     */

    public void setNomePais(String nomePais) {
        this.nomePais = nomePais;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getImagemMascote() {
        return foto_mascote;
    }
    public void setImagemMascote(String imagemMascote) {
        this.foto_mascote = imagemMascote;
    }

    public String getDescricaoMascote() {
        return descricao_mascote;
    }
    public void setDescricaoMascote(String descricaoMascote) {
        this.descricao_mascote = descricaoMascote;
    }

    public String getPais() {
        return nomePais;
    }
    public void setPais(String pais) {
        this.nomePais = pais;
    }

    public String getNomeMascote() {
        return nome_mascote;
    }
    public void setNomeMascote(String nomeMascote) {
        this.nome_mascote = nomeMascote;
    }

    public LocalDate getDataInicio() {
         return LocalDate.parse(String.valueOf(data_inicio));
    }
    public LocalDate getDataFim() {
        return LocalDate.parse(String.valueOf(data_fim));
    }
    public int getIdMascote() {
        return idMascote;
    }

    public void setIdMascote(int idMascote) {
        this.idMascote = idMascote;
    }

    public Image getImagemFileMascote() {
        return imageFileMascote;
    }

    public void setImagemFileMascote(Image image) {
        imageFileMascote = image;
    }
}


