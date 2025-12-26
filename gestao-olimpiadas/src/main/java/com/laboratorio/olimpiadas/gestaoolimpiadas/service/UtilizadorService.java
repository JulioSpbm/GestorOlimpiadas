package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.UtilizadorDAO;

import java.io.IOException;
import java.sql.SQLException;

public class UtilizadorService {
    /**
     * Obtem o ID do utilizador com base no número mecanográfico.
     * @param numMecanografico
     * @return
     * @throws SQLException
     */
    public static int getIdUserByNumMecanografico(String numMecanografico) throws SQLException, IOException {
        return UtilizadorDAO.getIdUserByNumMecanografico(numMecanografico);
    }

    public static String getNomeUserByNumMecan(String numMecanografico) throws SQLException {
        return UtilizadorDAO.getNomeUserByNumMecan(numMecanografico);
    }

    /**
     * Obtém o nome de um utilizador com base no ID fornecido.
     * @param idUtilizador
     * @return
     * @throws SQLException
     */
    public static String getNameUtilizadorById(int idUtilizador) throws SQLException {
        return UtilizadorDAO.getNomeUserById(idUtilizador);
    }

    /**
     * Inscreve uma equipa numa modalidade.
     * @param modalidadeId Modalidade ID
     * @param eventoID Evento id
     * @param equipaId Equipa id
     * @param aletaId Atleta ID
     */

    public void inscreverEquipa(int modalidadeId, int eventoID, int equipaId, int aletaId) {
        UtilizadorDAO.inscreverEquipaByIds(modalidadeId,eventoID,equipaId,aletaId);
    }

    /**
     * Inscreve um atleta numa modalidade.
     * @param modalidadeId Modalidade ID
     * @param eventoId Evento id
     * @param atletaId Atleta ID
     */

    public void inscreverModalidade(int modalidadeId, int eventoId, int atletaId) {
        UtilizadorDAO.inscreverModalidadeByIds(modalidadeId,eventoId,atletaId);
    }

    /**
     * Obtem o ID do utilizador com base no número mecanográfico.
     *
     * @param numMecanografico
     * @return
     * @throws SQLException
     */
    public String getFotoUserByNumMecanografico(String numMecanografico) throws SQLException, IOException {
        return UtilizadorDAO.getFotoUserByNumMecanografico(numMecanografico);
    }

    public boolean setFotoUserByNumMecanografico(String numMecanografico, String base64Image) throws SQLException, IOException {
        return UtilizadorDAO.setFotoUserByNumMecanografico(numMecanografico, base64Image);
    }
}
