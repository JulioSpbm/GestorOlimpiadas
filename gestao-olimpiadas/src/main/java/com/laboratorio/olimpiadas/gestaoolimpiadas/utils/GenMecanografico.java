package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.UtilizadorDAO;

import java.io.IOException;
import java.sql.SQLException;

public class GenMecanografico {

    /**
     * Gera o próximo número mecanográfico para o tipo de utilizador especificado.
     *
     * @param userType Tipo de utilizador (1 para gestores, 2 para utilizadores)
     * @return O próximo número mecanográfico disponível com o prefixo adequado (G para gestores, A para utilizadores).
     * @throws SQLException Se ocorrer um erro ao aceder à base de dados.
     */
    public static String generateNextMecanografico(int userType) throws SQLException, IOException {
        String prefix = userType == 1 ? "G" : "A";

        String lastNumMecanografico = UtilizadorDAO.getLastNumMecanografico(userType);
        int lastNum = Integer.parseInt(lastNumMecanografico.substring(1));;
        int nextNum = lastNum + 1;

        return prefix + nextNum;
    }
}