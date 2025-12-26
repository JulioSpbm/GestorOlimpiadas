package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.ResultadoDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Resultado;

import java.sql.SQLException;
import java.util.List;

public class ResultadoService {

    private static final ResultadoDAO resultadoDAO = new ResultadoDAO();

    public static int getIdMedalhaByName(String nome) throws SQLException {
        return resultadoDAO.getIdMedalhaByName(nome);
    }

    /**
     * Obtem todas as inscrições das modalidades
     * @return
     * @throws SQLException
     */
    public static List<Resultado> getModalidadeInscricoes() throws SQLException {
        return resultadoDAO.getAllModalidadeInscricoes();
    }

    /**
     * Guarda o resultado individual.
     * @param detalhe
     * @return
     */
    public static boolean saveResultadoIndividual(Resultado detalhe) {
       return resultadoDAO.saveResultadoIndividual(detalhe);
    }

    /**
     * Guarda o resultado da equipa.
     * @param detalhe
     * @return
     */
    public static boolean saveResultadoEquipa(Resultado detalhe) {
        return resultadoDAO.saveResultadoEquipa(detalhe);
    }

    /**
     * Obtém os resultados de uma equipa pelo ID.
     * @param idModalidade
     * @return
     */
    public static List<Resultado> getResultadoByModalidade(int idModalidade) {
        return resultadoDAO.getResultadoByModalidade(idModalidade);
    }
}
