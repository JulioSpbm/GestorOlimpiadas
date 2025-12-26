package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

/**
 * Representa uma Olimpíada com informações sobre o ano e o país anfitrião.
 */
public class Olimpiada {
    private int ano;
    private int idPais;

    /**
     * Constrói uma Olimpíada com o ano e o país anfitrião fornecidos.
     *
     * @param ano O ano da Olimpíada.
     * @param idPais O ID do país anfitrião da Olimpíada.
     */
    public Olimpiada(int ano, int idPais) {
        this.ano = ano;
        this.idPais = idPais;
    }

    /**
     * Retorna o ano da Olimpíada.
     *
     * @return O ano da Olimpíada.
     */
    public int getAno() {
        return ano;
    }

    /**
     * Define o ano da Olimpíada.
     *
     * @param ano O ano da Olimpíada.
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * Retorna o ID do país anfitrião da Olimpíada.
     *
     * @return O ID do país anfitrião da Olimpíada.
     */
    public int getIdPais() {
        return idPais;
    }

    /**
     * Define o ID do país anfitrião da Olimpíada.
     *
     * @param idPais O ID do país anfitrião da Olimpíada.
     */
    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

}
