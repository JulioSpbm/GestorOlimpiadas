package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

public class Participacao {

    private int anoParticipacao;
    private int ouro;
    private int prata;
    private int bronze;
    private String resultadoNome;

    public Participacao(int anoParticipacao, int ouro, int prata, int bronze) {
        this.anoParticipacao = anoParticipacao;
        this.ouro = ouro;
        this.prata = prata;
        this.bronze = bronze;
    }

    public Participacao(){}

    public int getAnoParticipacao() {
        return anoParticipacao;
    }

    public void setAnoParticipacao(int anoParticipacao) {
        this.anoParticipacao = anoParticipacao;
    }

    public int getOuro() {
        return ouro;
    }

    public void setOuro(int ouro) {
        this.ouro = ouro;
    }

    public int getPrata() {
        return prata;
    }

    public void setPrata(int prata) {
        this.prata = prata;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public String getResultadoNome(){return resultadoNome;}

    public void setResultadoNome(String resultadoNome){this.resultadoNome = resultadoNome;}

    @Override
    public String toString() {
        return "Participação {" +
                "Ano de Participação: " + anoParticipacao +
                ", Ouro: '" + ouro + '\'' +
                ", Prata: '" + prata + '\'' +
                ", Bronze: '" + bronze +
                '}';
    }
}
