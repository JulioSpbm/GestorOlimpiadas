package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventoDAO {

    /**
     * Insere um novo evento na base de dados.
     * @param evento Objeto que contém os dados do evento a ser inserido.
     * @return true se o evento for inserido com sucesso, false caso contrário.
     */
    public boolean createEvento(Evento evento) {
        String sql = "{CALL sp_inserir_evento(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, evento.getOlimpiada().getIdPais());
            stmt.setString(2, evento.getNome_mascote());
            stmt.setString(3, evento.getDescricao_mascote());
            stmt.setString(4, evento.getImagemMascote());
            stmt.setInt(5, evento.getOlimpiada().getAno());
            stmt.setDate(6, Date.valueOf(evento.getData_inicio()));
            stmt.setDate(7, Date.valueOf(evento.getData_fim()));
            stmt.setString(8, evento.getNome());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao inserir evento: " + e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtém a lista de países da base de dados.
     * @return Lista de países.
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

        } catch (SQLException | IOException e) {
            throw new SQLException("Erro ao obter países da base de dados: " + e.getMessage(), e);
        }

        return countries;
    }

    /**
     * Obtém o ID de um país com base no nome do país.
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

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter o ID do país: " + e.getMessage());
        }

        return -1;
    }

    /**
     * Obtém um evento com base no ID do mascote.
     * @param idMascote ID do mascote.
     * @return Evento associado ao mascote, ou null se o mascote não for encontrado.
     */
    public Evento getMascoteById(int idMascote) {
        String query = "{CALL sp_buscar_mascote_por_id(?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, idMascote);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Evento evento = new Evento();
                    evento.setIdMascote(rs.getInt("id_mascote"));
                    evento.setNome_mascote(rs.getString("nome"));
                    evento.setDescricao_mascote(rs.getString("descricao"));
                    evento.setImagemMascote(rs.getString("foto"));

                    return evento;
                } else {
                    AlertUtils.showErrorAlert("Aviso!", "Nenhum evento encontrado com o ID do mascote: " + idMascote);
                }
            }

        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao obter evento por ID do mascote: " + e.getMessage());
        }

        return null;
    }


    public List<Evento> listarEventos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "{CALL sp_buscar_evento}";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evento evento = new Evento();

                evento.setId(rs.getInt("id_evento"));
                evento.setIdMascote(rs.getInt("id_mascote"));
                evento.setOlimpiada(new Olimpiada(rs.getInt("ano"), rs.getInt("id_pais")));
                evento.setNome(rs.getString("nome"));
                evento.setAno(rs.getString("ano"));
                evento.setNome_mascote(rs.getString("mascote"));
                evento.setData_inicio(rs.getDate("data_inicio"));
                evento.setData_fim(rs.getDate("data_fim"));
                evento.setNomePais(rs.getString("pais"));
                evento.setImagemMascote(rs.getString("fotoMascote"));

                eventos.add(evento);
            }
            return eventos;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Edita um evento.
     *
     * @param evento
     * @return
     */
    public boolean updateEvento(Evento evento) {
        String sql = "{CALL sp_editar_evento(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, evento.getId());
            stmt.setInt(2, evento.getOlimpiada().getIdPais());
            stmt.setString(3, evento.getNome_mascote());
            stmt.setString(4, evento.getDescricao_mascote());
            stmt.setString(5, evento.getImagemMascote());
            stmt.setInt(6, evento.getOlimpiada().getAno());
            stmt.setDate(7, Date.valueOf(evento.getData_inicio()));
            stmt.setDate(8, Date.valueOf(evento.getData_fim()));
            stmt.setString(9, evento.getNome());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Lista modalidades.
     *
     * @param id id Evento
     * @return modalidades Lista de Modalidades
     */

    public List<Modalidade> getModalidadesByIdEvent(int id) {
        String query = "{call sp_buscar_modalidade_por_evento(?)}";
        List<Modalidade> modalidades = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Modalidade modalidade = new Modalidade();
                modalidade.setId(resultSet.getInt("id_modalidade"));
                modalidade.setNome(resultSet.getString("nome"));
                modalidade.setGeneroNome(resultSet.getString("genero"));
                modalidade.setMedidaPontuacaoNome(resultSet.getString("medida_pontuacao"));

                modalidades.add(modalidade); // Adiciona a modalidade à lista
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter modalidades: " + e.getMessage());
        }

        return modalidades;
    }

    /**
     * Lista equipas.
     *
     * @param id id Modalidade
     * @param eventoId id Evento
     * @param generoNome Nome do genero
     * @return equipas Lista de equipas
     */
    public List<Equipa> getEquipasByIdModalidadeEvento(int id, int eventoId, String generoNome) {
        String query = "{call sp_buscar_equipa_por_modalidade_e_evento(?,?,?)}";
        List<Equipa> equipas = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setInt(2,eventoId);
            int genero = -1;
            if ("Masculino".equals(generoNome)) {
                genero = 0;
            } else if ("Feminino".equals(generoNome)) {
                genero = 1;
            }
            stmt.setInt(3,genero);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Equipa equipa = new Equipa();
                equipa.setId(resultSet.getInt("id_equipa"));
                equipa.setNome(resultSet.getString("nome_equipa"));
                equipa.setGeneroNome(resultSet.getString("genero"));
                equipa.setPaisNome(resultSet.getString("nome_pais"));

                equipas.add(equipa);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter modalidades: " + e.getMessage());
        }

        return equipas;
    }

    public List<Atleta> getAtletasByIdModalidadeEvento(int id, int eventoId, String generoNome) {
        String query = "{call sp_buscar_atleta_por_modalidade_e_evento(?,?,?)}";
        List<Atleta> atletas = new ArrayList<>();

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setInt(2,eventoId);
            int genero = -1;
            if ("Masculino".equals(generoNome)) {
                genero = 0;
            } else if ("Feminino".equals(generoNome)) {
                genero = 1;
            }
            stmt.setInt(3,genero);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Atleta atleta = new Atleta();
                atleta.setId(resultSet.getInt("id_utilizador"));
                atleta.setNome(resultSet.getString("nome_atleta"));
                atleta.setGeneroNome(resultSet.getString("genero"));

                atletas.add(atleta);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter modalidades: " + e.getMessage());
        }

        return atletas;
    }

    public int getCapacidadeLocalPorModalidade(int idModalidade) {
        String query = "{call sp_buscar_local_modalidade_por_evento(?)}";
        int capacaidade = 0;

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1,idModalidade);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                capacaidade = resultSet.getInt("capacidade");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter modalidades: " + e.getMessage());
        }

        return capacaidade;
    }


    public void addModalidade(int idEvent, int idModalidade) {
        String sql = "{CALL sp_inserir_evento_modalidade(?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvent);
            stmt.setInt(2, idModalidade);

            stmt.executeUpdate();
            AlertUtils.showInfoAlert("Sucesso!", "Modalidade inserida com sucesso!");
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao inserir Modalidade: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMatch(int idEvento, int idModalidade, int idLocal, Timestamp dataHoraFormatada) {
        String sql = "{CALL sp_inserir_local_evento_modalidade(?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idEvento);
            stmt.setInt(2, idLocal);
            stmt.setInt(3, idModalidade);
            stmt.setTimestamp(4, dataHoraFormatada);
            stmt.executeUpdate();
            AlertUtils.showInfoAlert("Sucess!", "Match created!");
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Error!", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
