package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Resultado;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultadoDAO {
    /**
     * Obtém o ID da medalha pelo ID.
     * @param nome
     * @return
     */
    public static int getIdMedalhaByName(String nome) {
        String sql = "SELECT id_medalha FROM tab_medalha WHERE medalha = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id_medalha");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    /**
     * Obtém uma lista de todas as modalidades e inscrições.
     * @return
     * @throws SQLException
     */
    public List<Resultado> getAllModalidadeInscricoes() throws SQLException {
        List<Resultado> modalidadeInscricoesList = new ArrayList<>();

        String query = "{call sp_buscar_modalidade_inscricoes}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             CallableStatement stmt = connection.prepareCall(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                List<String> ids = null;
                int idModalidade = rs.getInt("IdModalidade");
                int idEvento = rs.getInt("IdEvento");
                String modalidade = rs.getString("Modalidade");
                String genero = rs.getString("Genero");
                String tipo = rs.getString("Tipo");
                String tipoMedida = rs.getString("Tipo_Medida");
                int numeroInscricoes = rs.getInt("Numero_Inscricoes");
                String idsString = rs.getString("IDs");
                if (idsString != null){
                    ids = Arrays.asList(idsString.split(",\\s*"));
                }

                Resultado resultado = new Resultado(modalidade,genero,tipo,tipoMedida,numeroInscricoes,ids);

                resultado.setIdModalidade(idModalidade);
                resultado.setIdEvento(idEvento);

                modalidadeInscricoesList.add(resultado);
            }

        } catch (SQLException | IOException e) {
            throw new SQLException("Erro ao obter modalidades e inscrições da base de dados: " + e.getMessage(), e);
        }

        return modalidadeInscricoesList;
    }

    /**
     * Guarda o resultado do atleta numa modalidade.
     * @param detalhe
     * @return
     */
    public boolean saveResultadoIndividual(Resultado detalhe) {
        String sql = "{CALL sp_inserir_resultado(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalhe.getIdModalidade());
            stmt.setInt(2, detalhe.getIdAtleta());
            stmt.setNull(3, Types.INTEGER);
            stmt.setInt(4, detalhe.getIdMedalha());
            stmt.setDate(5, Date.valueOf(detalhe.getDataResultado()));
            stmt.setDouble(6, detalhe.getPontuacaoIndividual());
            stmt.setInt(7, detalhe.getIdEvento());


            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException | IOException e) {
            return false;
        }
    }

    /**
     * Guarda o resultado da equipa numa modalidade.
     * @param detalhe
     * @return
     */
    public boolean saveResultadoEquipa(Resultado detalhe) {
        String sql = "{CALL sp_inserir_resultado(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalhe.getIdModalidade());
            stmt.setNull(2, Types.INTEGER);
            stmt.setInt(3, detalhe.getIdEquipa());
            stmt.setInt(4, detalhe.getIdMedalha());
            stmt.setDate(5, Date.valueOf(detalhe.getDataResultado()));
            stmt.setDouble(6, detalhe.getPontuacaoTeam());
            stmt.setInt(7, detalhe.getIdEvento());


            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao inserir atleta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carrega e retorna os resultados de uma modalidade.
     * @param idModalidade
     * @return
     */
    public List<Resultado> getResultadoByModalidade(int idModalidade) {
        String sql = "{CALL sp_buscar_resultado_por_modalidade (?)}";
        List<Resultado> resultados = new ArrayList<>();

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idModalidade);

            ResultSet rs = stmt.executeQuery();

            // Altera o if para um while para percorrer todos os resultados
            while (rs.next()) {
                Resultado resultado = new Resultado();
                resultado.setNomeModalidade(rs.getString("modalidade"));
                resultado.setNomeAtleta(rs.getString("utilizador"));
                resultado.setNomeEquipa(rs.getString("equipa"));
                resultado.setNomeMedalha(rs.getString("medalha"));
                resultado.setDataResultado(String.valueOf(rs.getDate("data_resultado")));
                resultado.setPontuacao(String.valueOf(rs.getDouble("resultado")));
                resultado.setNomeEvento(rs.getString("evento"));

                resultados.add(resultado);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return resultados.isEmpty() ? null : resultados;
    }


}
