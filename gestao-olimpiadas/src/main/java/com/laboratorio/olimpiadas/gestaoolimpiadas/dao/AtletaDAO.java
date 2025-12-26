package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso aos dados do Atleta. Permite realizar operações CRUD na base de dados.
 */
public class AtletaDAO {

    /**
     * Cria um novo atleta na base de dados.
     * @param atleta Objeto Atleta a ser inserido.
     * @return true se o atleta foi criado com sucesso, false caso contrário.
     */
    public boolean createAtleta(Atleta atleta) {
        String sql = "{CALL sp_inserir_atleta(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, atleta.getidPais());
            stmt.setString(2, atleta.getNome());
            stmt.setDate(3, Date.valueOf(atleta.getDataNascimento()));
            stmt.setBoolean(4, atleta.getGenero());
            stmt.setInt(5, atleta.getAltura());
            stmt.setInt(6, atleta.getPeso());
            stmt.setString(7, atleta.getUtilizador().getPalavraPasse());
            stmt.setString(8, atleta.getUtilizador().getUsername());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException | NullPointerException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cria uma nova particiapcao na base de dados.
     * @param participacao Objeto Participacao a ser inserido.
     */
    public boolean createParticipacao(Participacao participacao) {
        String sql = "{CALL sp_inserir_participacao_atleta(?, ?, ?, ?)}";


        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, participacao.getAnoParticipacao());
            stmt.setInt(2, participacao.getOuro());
            stmt.setInt(3, participacao.getPrata());
            stmt.setInt(4, participacao.getBronze());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            return false;
        }
    }

    /**
     * Obtém a lista de todos os países disponíveis na base de dados.
     * @return Lista de países como String.
     * @throws SQLException Se ocorrer um erro de acesso à base de dados.
     */
    public List<String> getAllCountries() throws SQLException {
        List<String> countries = new ArrayList<>();
        String query = "SELECT pais FROM tab_pais";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                countries.add(rs.getString("pais"));
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao obter países da base de dados: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return countries;
    }

    /**
     * Obtém o ID de um país com base no nome.
     * @param nomePais Nome do país.
     * @return ID do país, ou -1 se o país não for encontrado.
     */
    public int getPaisIdByName(String nomePais) {
        String query = "SELECT id_pais FROM tab_pais WHERE pais = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nomePais);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_pais");
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter o ID do país: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    /**
     * Obtém o ID de um país com base no iso.
     * @param isoPais Iso do país.
     * @return ID do país, ou -1 se o país não for encontrado.
     */

    public int getPaisIdByIsoName(String isoPais) {
        String query = "SELECT id_pais FROM tab_pais WHERE iso = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, isoPais);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_pais");
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter o ID do país: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    /**
     * Lista todos os atletas da base de dados.
     */
    public List<Atleta> listarAtletas() {
        List<Atleta> atletas = new ArrayList<>();
        String sql = "{CALL sp_buscar_atletas}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Atleta atleta = new Atleta();
                atleta.setNome(rs.getString("nome_utilizador"));
                atleta.setPaisNome(rs.getString("pais"));
                atleta.setDataNascimento(rs.getDate("data_nascimento"));
                atleta.setGeneroNome(rs.getString("genero"));
                atleta.setAltura(rs.getInt("altura"));
                atleta.setPeso(rs.getInt("peso"));
                atleta.setId(rs.getInt("id_utilizador"));
                atleta.setidPais(rs.getInt("id_pais"));
                atleta.setEstado(rs.getString("estado"));
                atletas.add(atleta);
            }
            return atletas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Atualiza os dados do Atleta.
     */
    public boolean updateAtleta(Atleta atleta) {
        String sql = "{CALL sp_editar_atleta(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, atleta.getId());
            stmt.setInt(2, atleta.getidPais());
            stmt.setString(3, atleta.getNome());
            stmt.setDate(4, Date.valueOf(atleta.getDataNascimento()));
            stmt.setBoolean(5, atleta.getGenero());
            stmt.setInt(6, atleta.getAltura());
            stmt.setInt(7, atleta.getPeso());
            String estado = atleta.getEstado();
            boolean estadoBool = estado.equals("Ativado");
            stmt.setBoolean(8, estadoBool);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Error!", "Error updating an athlete: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista dados do atleta com o numero mecanografico.
     * @param authenticatedUser Numero mecanografico
     */

    public Atleta getAtletaByNumMecanografico(String authenticatedUser) {
        String query = "{call sp_buscar_atleta_por_num_mecanografico(?)}";
        Atleta atleta = null;

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, authenticatedUser);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                atleta = new Atleta();
                atleta.setId(resultSet.getInt("id_utilizador"));
                atleta.setPaisNome(resultSet.getString("pais"));
                atleta.setGeneroNome(resultSet.getString("genero"));
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter atleta: " + e.getMessage());
        }

        return atleta;
    }

    public Atleta getAtletadetails(String authenticatedUser) {
        String query = "{call sp_buscar_atleta_por_num_mecanografico(?)}";

        Atleta atleta = null;
        List<Integer> anosParticipacao = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, authenticatedUser.trim());

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                if (atleta == null) {
                    atleta = new Atleta();
                    atleta.setId(resultSet.getInt("id_utilizador"));
                    atleta.setNome(resultSet.getString("nome"));
                    atleta.setAltura(resultSet.getInt("altura"));
                    atleta.setPeso(resultSet.getInt("peso"));
                    atleta.setPaisNome(resultSet.getString("pais"));
                    atleta.setGenero(resultSet.getBoolean("genero"));
                }

                String anosString = resultSet.getString("ano");
                if (anosString != null && !anosString.isEmpty()) {
                    String[] anosArray = anosString.split(",");
                    for (String ano : anosArray) {
                        anosParticipacao.add(Integer.parseInt(ano.trim()));
                    }
                }
            }

            if (atleta != null) {
                atleta.setAnosParticipacao(anosParticipacao);
            } 
        } catch (SQLException | IOException | NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter atleta: " + e.getMessage());
        }
        return atleta;
    }





    public static List<AtletaRecorde> getAthleteComRecordes(){
        List<AtletaRecorde> records = new ArrayList<>();
        String sql = "SELECT r.utilizador AS atleta, r.recorde_olimpico AS recorde, r.ano FROM tab_recorde_olimpico r WHERE r.utilizador IS NOT NULL AND r.equipa IS NULL AND r.recorde_olimpico IS NOT NULL;";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                AtletaRecorde record = new AtletaRecorde();
                record.setAtleta(rs.getString("atleta"));
                record.setRecorde(rs.getFloat("recorde"));
                record.setAno(rs.getInt("ano"));
                records.add(record);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao encontrar Atletas com recordes: " + e.getMessage());
        }
        return records;
    }

    public List<AtletaRecorde> getHistoricoAtleta(String authenticatedUser) {
        String query = "{call sp_buscar_recorde_atleta(?)}";
        List<AtletaRecorde> recordes = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, authenticatedUser);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                AtletaRecorde recorde = new AtletaRecorde();
                recorde.setNome_modalidade(resultSet.getString("nome_modalidade"));
                recorde.setAno(resultSet.getInt("ano"));
                recorde.setRecorde(resultSet.getFloat("recorde_olimpico"));
                recorde.setNum_medalhas(resultSet.getInt("num_medalhas"));

                recordes.add(recorde);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter recordes: " + e.getMessage());
        }

        return recordes;
    }
}
