package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.LocalDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Classe de serviço para lidar com operações de negócio relacionadas com locais.
 */
public class LocalService {

    private final LocalDAO localDAO;

    public LocalService() {
        this.localDAO = new LocalDAO();
    }

    /**
     * Insere um novo local com os parâmetros fornecidos.
     * @param nome Nome do local.
     * @param morada Morada do local.
     * @param cidade Cidade onde o local se situa.
     * @param capacidade Capacidade máxima de pessoas que o local suporta.
     * @param anoConstrucao Ano de construção do local.
     * @return true se o local for inserido com sucesso; caso contrário, false.
     */
    public static boolean insertLocal(String nome, String morada, String cidade, String capacidade, String anoConstrucao, String tipoLocall) {

        int capacidadee = Integer.parseInt(capacidade);
        int anoConstrucaoo = Integer.parseInt(anoConstrucao);
        boolean tipoLocal = tipoLocall.equalsIgnoreCase("Interior");
        Local local = new Local( nome, morada, cidade, capacidadee, anoConstrucaoo, tipoLocal);

        return LocalDAO.createLocal(local);
    }

    /**
     * Edita um local com os parâmetros fornecidos.
     * @param id ID do local a ser editado.
     * @param nome Nome do local.
     * @param morada Morada do local.
     * @param cidade Cidade onde o local se situa.
     * @param capacidade Capacidade máxima de pessoas que o local suporta.
     * @param anoConstrucao Ano de construção do local.
     * @return true se o local for editado com sucesso; caso contrário, false.
     */
    public static boolean updateLocal(int id, String nome, String morada, String cidade, String capacidade, String anoConstrucao, String tipoLocall) {

        int capacidadee = Integer.parseInt(capacidade);
        int anoConstrucaoo = Integer.parseInt(anoConstrucao);
        Local local = new Local(id, nome, morada, cidade, capacidadee, anoConstrucaoo, tipoLocall);
        return LocalDAO.updateLocal(local);
    }

    public List<Local> listarLocais() {
        return localDAO.listarLocais();
    }

    public ObservableList<String> listarTipoLocais() {
        return localDAO.listarTipoLocais();
    }
}
