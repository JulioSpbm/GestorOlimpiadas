package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

public class Utilizador {
    private String username;
    private String palavraPasse;
    private int tipo;

    public Utilizador(String username, String palavraPasse, int tipo) {
        this.username = username;
        this.palavraPasse = palavraPasse;
        this.tipo = tipo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPalavraPasse() {
        return palavraPasse;
    }

    public void setPalavraPasse(String palavraPasse) {
        this.palavraPasse = palavraPasse;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}