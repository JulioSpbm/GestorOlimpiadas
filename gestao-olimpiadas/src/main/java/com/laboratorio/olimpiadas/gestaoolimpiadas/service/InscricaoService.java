package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.InscricaoDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.LocalDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Inscricao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;

import java.sql.SQLException;
import java.util.List;

public class InscricaoService {

    private final InscricaoDAO inscricaoDAO;

    public InscricaoService() { this.inscricaoDAO = new InscricaoDAO(); }

    public List<Inscricao> listarInscricoes() {
        return inscricaoDAO.listarInscricoes();
    }

    /**
     * Atualiza o estado da inscrição.
     * @param id
     * @param estado
     * @return
     */
    public static boolean atualizarEstado(int id, String estado) {
        int estadoBoolean = estado.equalsIgnoreCase("Aprovado") ? 1 : 0;
        return InscricaoDAO.atualizarEstado(id, estadoBoolean);
    }


}
