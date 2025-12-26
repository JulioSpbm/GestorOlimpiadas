package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Partida;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaDAO {

    public List<Partida> getAllMatches() throws SQLException {
        List<Partida> partidaList = new ArrayList<>();

        String query = "{call sp_listar_local_evento_modalidade}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idEvento = rs.getInt("id_evento");
                int idLocal = rs.getInt("id_local");
                int idModalidade = rs.getInt("id_modalidade");
                Timestamp data = rs.getTimestamp("data_hora");

                Partida partida = new Partida(idEvento,idLocal,idModalidade,data);

                partida.setEventoNome(rs.getString("nome_evento"));
                partida.setLocalNome(rs.getString("nome_local"));
                partida.setModalidadeNome(rs.getString("nome_modalidade"));

                partidaList.add(partida);
            }

        } catch (SQLException | IOException e) {
            throw new SQLException("Erro ao obter partidas: " + e.getMessage(), e);
        }

        return partidaList;
    }

    public void deleteMatch(Partida selectedPartida) {
        selectedPartida.getId_evento();
        selectedPartida.getId_local();
        selectedPartida.getId_modalidade();
        selectedPartida.getData_hora();
    }

    public List<Partida> getAllMatchesByUtilizador(String num_mecanografico) throws SQLException {
        List<Partida> partidaList = new ArrayList<>();

        // Chamada ao procedimento armazenado com parâmetro de filtro
        String query = "{call sp_listar_local_evento_modalidade_por_utilizador(?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {

            // Define o parâmetro de entrada (id_utilizador)
            stmt.setString(1, num_mecanografico);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idEvento = rs.getInt("id_evento");
                    int idLocal = rs.getInt("id_local");
                    int idModalidade = rs.getInt("id_modalidade");
                    Timestamp data = rs.getTimestamp("data_hora");

                    Partida partida = new Partida(idEvento, idLocal, idModalidade, data);

                    partida.setEventoNome(rs.getString("nome_evento"));
                    partida.setLocalNome(rs.getString("nome_local"));
                    partida.setModalidadeNome(rs.getString("nome_modalidade"));

                    partidaList.add(partida);
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao obter partidas do utilizador: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return partidaList;
    }

}
