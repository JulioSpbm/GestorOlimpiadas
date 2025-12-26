package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

/**
 * Representa uma modalidade nos Jogos Olímpicos.
 * Contém as informações detalhadas sobre uma modalidade, como o nome, descrição, número mínimo de participantes,
 * tipo, género, pontuação, número de jogos, recordes e regras.
 */
public class Modalidade {
    private boolean tipo;
    private boolean genero;
    private String nome;
    private String descricao;
    private int minParticipantes;
    private int idPontuacao;
    private int numJogos;
    private String numJogosNome;
    private String regras;
    private String generoNome;
    private String tipoNome;
    private String medidaPontuacaoNome;
    private RecordeModalidade recordeOlimpico;
    private VencedorOlimpico vencedorOlimpico;
    private int idModalidade;
    private int id;
    private String estado;
    private boolean estadoBool;

    private String recordeOlimpicoNome;
    private String vencedorOlimpicoNome;



    /**
     * Construtor da Classe Modalidade
     *
     * @param tipo O tipo da modalidade (individual ou equipa).
     * @param genero O género da modalidade (masculino ou feminino).
     * @param nome O nome da modalidade.
     * @param descricao A descrição da modalidade.
     * @param minParticipantes O número mínimo de participantes.
     * @param idPontuacao A pontuação associada à modalidade.
     * @param numJogos O número de jogos.
     * @param recordeOlimpico O recorde da modalidade.
     * @param regras As regras da modalidade.
     */

    public Modalidade(boolean tipo, boolean genero, String nome, String descricao, int minParticipantes, int idPontuacao, int numJogos, String regras, String estado) {
        this.tipo = tipo;
        this.genero = genero;
        this.nome = nome;
        this.descricao = descricao;
        this.minParticipantes = minParticipantes;
        this.idPontuacao = idPontuacao;
        this.numJogos = numJogos;
        this.regras = regras;
        this.estado = estado;
    }

    public Modalidade(boolean tipo, boolean genero, String nome, String descricao, int minParticipantes, int idPontuacao, int numJogos, String regras) {

        this.tipo = tipo;
        this.genero = genero;
        this.nome = nome;
        this.descricao = descricao;
        this.minParticipantes = minParticipantes;
        this.idPontuacao = idPontuacao;
        this.numJogos = numJogos;
        this.regras = regras;
    }

    public Modalidade() {}



    /**
     * Obtém o número mínimo de participantes para a modalidade.
     * @return O número mínimo de participantes.
     */
    public int getMinParticipantes() {
        return minParticipantes;
    }

    /**
     * Define o número mínimo de participantes para a modalidade.
     * @param minParticipantes O número mínimo de participantes.
     */
    public void setMinParticipantes(int minParticipantes) {
        this.minParticipantes = minParticipantes;
    }

    /**
     * Obtém o nome da modalidade.
     * @return O nome da modalidade.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da modalidade.
     * @param nome O nome a ser atribuído à modalidade.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição da modalidade.
     * @return A descrição da modalidade.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição da modalidade.
     * @param descricao A descrição a ser atribuída à modalidade.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o número de jogos associados à modalidade.
     * @return O número de jogos.
     */
    public int getNumJogos() {
        return numJogos;
    }

    /**
     * Define o número de jogos associados à modalidade.
     * @param numJogos O número de jogos.
     */
    public void setNumJogos(int numJogos) {
        this.numJogos = numJogos;
    }


    /**
     * Obtém o número de jogos associados à modalidade.
     * @return O número de jogos.
     */
    public String getNumJogosNome() {
        return numJogosNome;
    }

    /**
     * Define o número de jogos associados à modalidade.
     * @param numJogosNome O número de jogos.
     */
    public void setNumJogosNome(String numJogosNome) {
        this.numJogosNome = numJogosNome;
    }


    /**
     * Obtém o género da modalidade.
     * @return true se a modalidade é masculina; false se é feminina.
     */
    public boolean getGenero() {
        return genero;
    }

    /**
     * Define o género da modalidade.
     * @param genero true para modalidade masculina, false para feminina.
     */
    public void setGenero(boolean genero) {
        this.genero = genero;
    }

    /**
     * Obtém o tipo da modalidade (individual ou equipa).
     * @return true se é uma modalidade de equipa; false se é individual.
     */
    public boolean getTipo() {
        return tipo;
    }

    /**
     * Define o tipo da modalidade.
     * @param tipo true para modalidade de equipa, false para individual.
     */
    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtém o ID da pontuação associada à modalidade.
     * @return O ID da pontuação.
     */
    public int getIdPontuacao() {
        return idPontuacao;
    }

    /**
     * Define o ID da pontuação associada à modalidade.
     * @param idPontuacao O ID da pontuação.
     */
    public void setIdPontuacao(int idPontuacao) {
        this.idPontuacao = idPontuacao;
    }

    /**
     * Obtém o recorde olímpico da modalidade.
     *
     * @return O recorde olímpico.
     */
    public RecordeModalidade getRecordeOlimpico(){
        return recordeOlimpico;
    }

    public String getRecordeOlimpicoNome(){
        if (recordeOlimpico.getUtilizadorNome() != null){
            return recordeOlimpico.getUtilizadorNome();
        }else  {
            return recordeOlimpico.getEquipaNome();
        }
    }

    public String getRecordeOlimpicoRecord(){
        if (recordeOlimpico.getRecord_olimpicoNome() == null){
            return recordeOlimpico.getNum_medalhasNome();
        }else  {
            return recordeOlimpico.getRecord_olimpicoNome();
        }
    }

    public int getRecordeOlimpicoAno(){
        return recordeOlimpico.getAno_recorde();
    }

    /**
     * Define o recorde olímpico da modalidade.
     * @param recordeOlimpico O valor do recorde olímpico.
     */
    public void setRecordeOlimpico(RecordeModalidade recordeOlimpico) {
        this.recordeOlimpico = recordeOlimpico;
    }

    /**
     * Obtém o recorde olímpico da modalidade.
     * @return O recorde olímpico.
     */
    public VencedorOlimpico getVencedorOlimpico(){
        return vencedorOlimpico;
    }

    public String getVencedorOlimpicoNome(){
        if (vencedorOlimpico.getUtilizadorNome() != null){
            return vencedorOlimpico.getUtilizadorNome();
        }else  {
            return vencedorOlimpico.getEquipaNome();
        }
    }

    public String getVencedorOlimpicoRecord(){
        if (vencedorOlimpico.getResultadoNome() == null){
            return String.valueOf(vencedorOlimpico.getMedalhaNome());
        }else  {
            return String.valueOf(vencedorOlimpico.getResultadoNome());
        }
    }

    public int getVencedorOlimpicoAno(){
        return vencedorOlimpico.getAno_vencedor();
    }

    /**
     * Define o recorde olímpico da modalidade.
     * @param vencedorOlimpico O valor do recorde olímpico.
     */
    public void setVencedorOlimpico(VencedorOlimpico vencedorOlimpico) {
        this.vencedorOlimpico = vencedorOlimpico;
    }

    /**
     * Obtém as regras da modalidade.
     * @return As regras da modalidade.
     */
    public String getRegras() {
        return regras;
    }

    /**
     * Define as regras da modalidade.
     * @param regras As regras a serem atribuídas à modalidade.
     */
    public void setRegras(String regras) {
        this.regras = regras;
    }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public boolean getEstadoBool() { return estadoBool; }

    public void setEstadoBool(boolean estadoBool) { this.estadoBool = estadoBool; }

    public String getGeneroNome() {
        return generoNome;
    }

    public void setGeneroNome(String generoNome) {
        this.generoNome = generoNome;
    }

    public String getTipoNome() {
        return tipoNome;
    }

    public void setTipoNome(String tipoNome) {
        this.tipoNome = tipoNome;
    }

    public String getPontuacaoTipo() {
        return medidaPontuacaoNome;
    }

    public void setPontuacaoTipo(String medidaPontuacaoNome) {
        this.medidaPontuacaoNome = medidaPontuacaoNome;
    }

    public int getIdModalidade() {return idModalidade;}
    public void setIdModalidade(int idModalidade) {this.idModalidade = idModalidade;}

    public String getMedidaPontuacaoNome() {
        return medidaPontuacaoNome;
    }
    public void setMedidaPontuacaoNome(String medidaPontuacaoNome) {
        this.medidaPontuacaoNome = medidaPontuacaoNome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}