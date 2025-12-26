package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.AtletaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.XMLImporterDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.XML;

import java.util.List;

public class XMLImporterService {

    private static final XMLImporterDAO xmlDAO = new XMLImporterDAO();

    /**
     * Obtém uma lista de históricos de importação de dados XML.
     *
     * @return Uma lista de objetos {@link XML}.
     */
    public List<XML> listarHist() {
        return xmlDAO.listarHist();
    }
}
