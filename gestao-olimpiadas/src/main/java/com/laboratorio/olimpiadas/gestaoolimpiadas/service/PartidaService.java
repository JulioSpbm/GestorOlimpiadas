package com.laboratorio.olimpiadas.gestaoolimpiadas.service;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.PartidaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.ResultadoDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Partida;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Resultado;

import java.sql.SQLException;
import java.util.List;

public class PartidaService {

    private static final PartidaDAO partidaDAO = new PartidaDAO();


    public static List<Partida> getAllMatches() throws SQLException {
        return partidaDAO.getAllMatches();
    }

    public static List<Partida> getAllMatchesByUtilizador(String authenticatedUser) throws SQLException {
        return partidaDAO.getAllMatchesByUtilizador(authenticatedUser);
    }

    public void deleteMatch(Partida selectedPartida) {
        partidaDAO.deleteMatch(selectedPartida);
    }
}
