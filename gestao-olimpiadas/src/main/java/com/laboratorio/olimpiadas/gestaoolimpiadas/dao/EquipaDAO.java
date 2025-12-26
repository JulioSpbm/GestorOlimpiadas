package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.AtletaRecorde;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Equipa;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.EquipaRecorde;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pela interação com a base de dados para a entidade Equipa.
 */
public class EquipaDAO {

    /**
     * Cria uma nova equipa na base de dados.
     *
     * @param equipa Objeto Equipa contendo os dados da nova equipa.
     * @return true se a equipa foi criada com sucesso, false caso contrário.
     */
    public boolean createEquipa(Equipa equipa) {
        String sql = "{Call sp_inserir_equipa (?, ?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipa.getPais());
            stmt.setInt(2, equipa.getidModalidade());
            stmt.setBoolean(3, equipa.getGenero());
            stmt.setInt(4, equipa.getAnoFundacao());
            stmt.setString(5, equipa.getNome());


            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Criar uma nova equipa.
     * @param equipa
     */
    public void createEquipaXML(Equipa equipa) {
        String sql = "{Call sp_inserir_equipa_xml (?, ?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setString(1, equipa.getNome());
             stmt.setString(2, equipa.getPaisNome());
             stmt.setString(3, equipa.getGeneroNome());
             stmt.setString(4, equipa.getModalidadeNome());
             stmt.setInt(5, equipa.getAnoFundacao());

             stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Cria uma participação da equipa.
     * @param participacao
     */
    public void createParticipacaoTeam(Participacao participacao) {
        String sql = "{Call sp_inserir_participacao_equipa (?, ?)}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setInt(1, participacao.getAnoParticipacao());
             stmt.setString(2, participacao.getResultadoNome());

             stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao inserir equipa: " + e.getMessage());
        }
    }

    /**
     * Edita os dados das Equipas.
     * @param equipa
     * @return
     */
    public boolean editEquipa(Equipa equipa) {
        String sql = "{Call sp_editar_equipa (?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipa.getId());
            stmt.setInt(2, equipa.getPais());
            stmt.setInt(3, equipa.getidModalidade());
            stmt.setBoolean(4, equipa.getGenero());
            stmt.setInt(5, equipa.getAnoFundacao());
            stmt.setString(6, equipa.getNome());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao atualizar equipa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtém a lista de todos os países da base de dados.
     *
     * @return Lista de nomes dos países.
     */
    public List<String> getAllCountries() {
        List<String> countries = new ArrayList<>();
        String query = "SELECT pais FROM tab_pais";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                countries.add(rs.getString("pais"));
            }
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter países: " + e.getMessage());
        }

        return countries;
    }

    /**
     * Obtém o ID de um país pelo nome.
     *
     * @param nomePais Nome do país a procurar.
     * @return ID do país, ou -1 se não encontrado.
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

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter o id do pais: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtém a lista de todas as modalidades disponíveis na base de dados.
     *
     * @return Lista de nomes das modalidades.
     */
    public List<String> getAllModalidades() {
        List<String> modalidades = new ArrayList<>();
        String query = "SELECT nome FROM tab_modalidade";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modalidades.add(rs.getString("nome"));
            }

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter modalidades: " + e.getMessage());
        }
        return modalidades;
    }

    /**
     * Obtém o ID de uma modalidade pelo nome.
     *
     * @param modalidade Nome da modalidade a procurar.
     * @return ID da modalidade, ou -1 se não encontrado.
     */
    public int getModalidadeIdByName(String modalidade) {
        String query = "SELECT id_modalidade FROM tab_modalidade WHERE nome = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, modalidade);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_modalidade");
                } else {
                    return -1;
                }
            }

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter o id da modalidade: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Lista todas as equipas da base de dados.
     * @return
     */
    public List<Equipa> listarEquipas() {
        List<Equipa> equipas = new ArrayList<>();
        String sql = "{CALL sp_buscar_equipa}";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Equipa equipa = new Equipa();
                equipa.setPaisNome(rs.getString("pais"));
                equipa.setModalidadeNome(rs.getString("modalidade"));
                equipa.setGeneroNome(rs.getString("genero"));
                equipa.setNome(rs.getString("nome"));
                equipa.setAnoFundacao(rs.getInt("ano_fundacao"));

                equipas.add(equipa);
            }
            return equipas;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtém o nome do utilizador pelo seu nome de utilizador.
     *
     * @param id o nome de utilizador (username) a ser pesquisado.
     * @return o nome completo do utilizador, ou {@code null} se o utilizador não for encontrado.
     * @throws SQLException se ocorrer um erro ao aceder à base de dados.
     */
    public String getNameEquipaById(int id) throws SQLException, IOException {
        String sql = "SELECT nome FROM tab_equipa WHERE id_equipa = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nome");
            }
        }

        return null;
    }

    /**
     * Obtem o ID da equipa através do nome da mesma.
     * @param nome
     * @return
     */
    public int getIdEquipaByName(String nome) {
        String query = "SELECT id_equipa FROM tab_equipa WHERE nome = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nome);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_equipa");
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter o id da equipa: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public static List<EquipaRecorde> getTeamComRecordes(){
        List<EquipaRecorde> records = new ArrayList<>();
        String sql = "SELECT r.equipa AS equipa, r.recorde_olimpico AS recorde, r.ano FROM tab_recorde_olimpico r WHERE r.equipa IS NOT NULL AND r.utilizador IS NULL AND r.recorde_olimpico IS NOT NULL;";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EquipaRecorde record = new EquipaRecorde();
                record.setEquipa(rs.getString("equipa"));
                record.setRecorde(rs.getFloat("recorde"));
                record.setAno(rs.getInt("ano"));
                records.add(record);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao encontrar Equipas com recordes: " + e.getMessage());
        }
        return records;
    }

    public List<Equipa> listarEquipasFiltradas(int id_modalidade) {
        List<Equipa> equipas = new ArrayList<>();
        String sql = "{CALL sp_buscar_equipas_por_genero_modalidade (?)}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_modalidade);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Equipa equipa = new Equipa();
                    equipa.setId(rs.getInt("id_equipa"));
                    equipa.setPaisNome(rs.getString("pais"));
                    equipa.setModalidadeNome(rs.getString("modalidade"));
                    equipa.setGeneroNome(rs.getString("genero"));
                    equipa.setNome(rs.getString("nome"));
                    equipa.setAnoFundacao(rs.getInt("ano_fundacao"));
                    equipas.add(equipa);
                }
            }
            return equipas;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}