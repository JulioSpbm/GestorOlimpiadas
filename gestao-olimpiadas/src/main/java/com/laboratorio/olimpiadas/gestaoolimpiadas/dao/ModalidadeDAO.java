package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Modalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.ModalidadeRecorde;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.RecordeModalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.VencedorOlimpico;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class ModalidadeDAO {

    /**
     * cria uma modalidade na base de dados.
     * @param modalidade Objeto Modalidade que contém os dados a serem inseridos.
     * @return true se a modalidade for inserida com sucesso, caso contrário false.
     */
    public static boolean createModalidade(Modalidade modalidade) {
        String sql = "{CALL sp_inserir_modalidade(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, modalidade.getMinParticipantes());
            stmt.setString(2, modalidade.getNome());
            stmt.setString(3, modalidade.getDescricao());
            stmt.setInt(4, modalidade.getNumJogos());
            stmt.setBoolean(5, modalidade.getGenero());
            stmt.setBoolean(6, modalidade.getTipo());
            stmt.setInt(7, modalidade.getIdPontuacao());
            stmt.setString(8, modalidade.getRegras());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao inserir modalidade: " + e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Insere uma nova modalidade.
     * @param modalidade
     */
    public static void createModalidadeXML(Modalidade modalidade) {
        String sql = "{CALL sp_inserir_modalidade_xml(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, modalidade.getTipoNome());
            stmt.setString(2, modalidade.getGeneroNome());
            stmt.setString(3, modalidade.getNome());
            stmt.setString(4, modalidade.getDescricao());
            stmt.setInt(5, modalidade.getMinParticipantes());
            if (modalidade.getPontuacaoTipo().trim().equalsIgnoreCase("Distance")) {
                stmt.setString(6, "1");
            } else if (modalidade.getPontuacaoTipo().trim().equalsIgnoreCase("Time")) {
                stmt.setString(6, "2");
            } else if (modalidade.getPontuacaoTipo().trim().equalsIgnoreCase("Points")) {
                stmt.setString(6, "3");
            }
            stmt.setString(7, modalidade.getNumJogosNome());
            stmt.setString(8, modalidade.getRegras());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getPontuacaoIdByTipo(String pontuacaoTipo) {
        String sql = "SELECT id_pontuacao FROM tab_pontuacao WHERE medida_pontuacao = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, pontuacaoTipo.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return String.valueOf(rs.getInt("id_pontuacao"));
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erro ao obter ID da pontuação: " + e.getMessage());
                e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * edita uma modalidade na base de dados.
     * @param modalidade Objeto Modalidade que contém os dados a serem inseridos.
     * @return true se a modalidade for inserida com sucesso, caso contrário false.
     */
    public static boolean editModalidade(Modalidade modalidade, int id) {
        String sql = "{CALL sp_editar_modalidade(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, modalidade.getMinParticipantes());
            stmt.setString(3, modalidade.getNome());
            stmt.setString(4, modalidade.getDescricao());
            stmt.setInt(5, modalidade.getNumJogos());
            stmt.setBoolean(6, modalidade.getGenero());
            stmt.setBoolean(7, modalidade.getTipo());
            stmt.setInt(8, modalidade.getIdPontuacao());
            stmt.setString(9, modalidade.getRegras());
            stmt.setBoolean(10, modalidade.getEstadoBool());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao inserir modalidade: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insere um novo recorde olimpico.
     *
     * @param recordeOlimpico
     */
    public void createRecordOlimpico(RecordeModalidade recordeOlimpico) {
        String sql = "{CALL sp_inserir_recorde_olimpico(?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, recordeOlimpico.getUtilizadorNome());
            stmt.setString(2, recordeOlimpico.getEquipaNome());
            stmt.setInt(3, recordeOlimpico.getNum_medalhas());
            stmt.setFloat(4, recordeOlimpico.getRecord_olimpico());
            stmt.setInt(5, recordeOlimpico.getAno_recorde());

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro!","Erro ao inserir modalidade: " + e.getMessage());
        }
    }

    /**
     * Insere um novo vencedor olimpico
     * @param vencedorOlimpico
     */
    public void createVencedorOlimpico(VencedorOlimpico vencedorOlimpico) {
        String sql = "{CALL sp_inserir_vencedor_modalidade(?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, vencedorOlimpico.getUtilizadorNome());
            stmt.setString(2, vencedorOlimpico.getEquipaNome());
            stmt.setInt(3, vencedorOlimpico.getId_medalha());
            stmt.setFloat(4, vencedorOlimpico.getResultado());
            stmt.setInt(5, vencedorOlimpico.getAno_vencedor());



            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao inserir modalidade: " + e.getMessage());
        }
    }

    /**
     * Obtém uma lista de nomes de pontuações disponíveis na base de dados.
     * Esta lista é usada para preencher a lista de seleção de pontuações no formulário de criação de modalidades.
     * @return ObservableList com os nomes das pontuações.
     */
    public static ObservableList<String> getScores() {
        ObservableList<String> scores = FXCollections.observableArrayList();
        String query = "SELECT medida_pontuacao FROM tab_pontuacao";

        try (Connection connection = DBConnection.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                scores.add(rs.getString("medida_pontuacao"));
            }
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao obter pontuações: " + e.getMessage());
        }

        return scores;
    }

    /**
     * Obtém o ID da pontuação com base no nome da pontuação.
     *
     * @param score O nome da pontuação.
     * @return O ID da pontuação se encontrada, caso contrário empty.
     */
    public static OptionalInt getScoreIdByName(String score) {
        String query = "SELECT id_pontuacao FROM tab_pontuacao WHERE medida_pontuacao = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, score);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return OptionalInt.of(rs.getInt("id_pontuacao"));
                }
            }

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao obter pontuações: " + e.getMessage());
        }

        return OptionalInt.empty();
    }

    /**
     * Obtem uma lista dde todas as modalidades.
     * @return
     */
    public List<Modalidade> listarModalidades() {
        List<Modalidade> modalidades = new ArrayList<>();
        String sql = "{CALL sp_buscar_modalidades}";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Modalidade modalidade = new Modalidade();
                modalidade.setId(rs.getInt("id_modalidade"));
                modalidade.setNome(rs.getString("nome"));
                modalidade.setDescricao(rs.getString("descricao"));
                modalidade.setMinParticipantes(rs.getInt("min_participantes"));
                modalidade.setNumJogos(rs.getInt("num_jogos"));
                modalidade.setGeneroNome(rs.getString("genero"));
                modalidade.setTipoNome(rs.getString("tipo"));
                modalidade.setPontuacaoTipo(rs.getString("medida_pontuacao"));
                modalidade.setRegras(rs.getString("regras"));
                modalidade.setEstado(rs.getString("estado"));



                modalidades.add(modalidade);
            }
            return modalidades;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<ModalidadeRecorde> getModalidadesComRecordes() {
        List<ModalidadeRecorde> records = new ArrayList<>();
        String sql = "SELECT m.nome AS modalidade, r.recorde_olimpico AS recorde, r.ano " +
                "FROM tab_modalidade m " +
                "INNER JOIN tab_recorde_olimpico r ON m.id_modalidade = r.id_modalidade " +
                "WHERE r.recorde_olimpico IS NOT NULL";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ModalidadeRecorde record = new ModalidadeRecorde();
                record.setModalidade(rs.getString("modalidade"));
                record.setRecorde(rs.getFloat("recorde"));
                record.setAno(rs.getInt("ano"));
                records.add(record);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Erro", "Erro ao buscar modalidades com recordes: " + e.getMessage());
        }
        return records;
    }


    public List<Modalidade> listarModalidadesSemEvento() {
        List<Modalidade> modalidades = new ArrayList<>();
        String sql = "{CALL sp_buscar_modalidade_nao_associadas}";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Modalidade modalidade = new Modalidade();
                modalidade.setId(rs.getInt("id_modalidade"));
                modalidade.setNome(rs.getString("nome"));
                modalidade.setDescricao(rs.getString("descricao"));
                modalidade.setMinParticipantes(rs.getInt("min_participantes"));
                modalidade.setNumJogos(rs.getInt("num_jogos"));
                modalidade.setGeneroNome(rs.getString("genero"));
                modalidade.setTipoNome(rs.getString("tipo"));
                modalidade.setPontuacaoTipo(rs.getString("medida_pontuacao"));
                modalidade.setRegras(rs.getString("regras"));

                modalidades.add(modalidade);
            }
            return modalidades;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void addModalidadeEquipa(int idModalidade, int idEquipa) {
        String sql = "{CALL sp_inserir_modalidade_equipa(?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idModalidade);
            stmt.setInt(2, idEquipa);

            stmt.executeUpdate();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!","Erro ao inserir modalidade: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
