package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.PasswordUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilizadorDAO {

    /**
     * Verifica se o salt fornecido já existe na base de dados.
     *
     * @param salt o salt a ser verificado.
     * @return {@code true} se o salt for único, ou {@code false} caso contrário.
     * @throws SQLException se ocorrer um erro na consulta à base de dados.
     */
    public static boolean isSaltUnique(String salt) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tab_utilizador WHERE salt = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, salt);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Gera um salt único.
     *
     * @return um salt único.
     * @throws SQLException se ocorrer um erro na verificação da unicidade do salt.
     */
    public static String generateUniqueSalt() throws SQLException {
        String salt;
        do {
            salt = PasswordUtils.generateSalt();
        } while (!isSaltUnique(salt));

        return salt;
    }

    /**
     * Obtém o último número mecanográfico do utilizador de um tipo específico.
     *
     * @param userType Tipo de utilizador (1 para gestores, 2 para utilizadores).
     * @return O último número mecanográfico para o tipo de utilizador, ou o valor mínimo se não houver registos.
     * @throws SQLException Se ocorrer um erro ao consultar a base de dados.
     */
    public static String getLastNumMecanografico(int userType) throws SQLException, IOException {
        String sql = "SELECT MAX(num_mecanografico) FROM tab_utilizador WHERE id_tipo_utilizador = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userType);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(1);
            }

            return userType == 1 ? "G0" : "A0";
        }
    }

    /**
     * Obtém o nome do utilizador pelo seu nome de utilizador.
     *
     * @param num_mecanografico o nome de utilizador (username) a ser pesquisado.
     * @return o nome completo do utilizador, ou {@code null} se o utilizador não for encontrado.
     * @throws SQLException se ocorrer um erro ao aceder à base de dados.
     */
    public static String getNomeUserByNumMecan(String num_mecanografico) throws SQLException {
        String sql = "SELECT nome FROM tab_utilizador WHERE num_mecanografico = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, num_mecanografico);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nome");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Obtém o nome do utilizador pelo seu nome de utilizador.
     *
     * @param num_mecanografico o nome de utilizador (username) a ser pesquisado.
     * @return o nome completo do utilizador, ou {@code null} se o utilizador não for encontrado.
     * @throws SQLException se ocorrer um erro ao aceder à base de dados.
     */
    public static int getIdUserByNumMecanografico(String num_mecanografico) throws SQLException, IOException {
        String sql = "SELECT id_utilizador FROM tab_utilizador WHERE num_mecanografico = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, num_mecanografico);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id_utilizador");
            }
        }

        return -1;
    }

    public static String getFotoUserByNumMecanografico(String num_mecanografico) throws SQLException, IOException {
        String sql = "SELECT imagem FROM tab_utilizador WHERE num_mecanografico = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, num_mecanografico);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("imagem");
            }
        }

        return null;
    }

    public static boolean setFotoUserByNumMecanografico(String numMecanografico, String base64Image) throws SQLException {
        String sql = "UPDATE tab_utilizador SET imagem = ? WHERE num_mecanografico = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, base64Image);
            statement.setString(2, numMecanografico);

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtém o nome do utilizador pelo ID.
     *
     * @param idUtilizador
     * @return
     * @throws SQLException
     */
    public static String getNomeUserById(int idUtilizador) throws SQLException {
        String sql = "SELECT nome FROM tab_utilizador WHERE id_utilizador = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idUtilizador);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nome");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Faz um inscrição pelos IDs.
     *
     * @param modalidadeId Modalidade id
     * @param eventoID Evento id
     * @param equipaId Equipa id
     * @param atletaId Atleta id
     * @throws SQLException erro se ja existe
     */

    public static void inscreverEquipaByIds(int modalidadeId, int eventoID, int equipaId, int atletaId) {
        String query = "{call sp_inserir_inscricao(?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, atletaId);
            stmt.setInt(2, equipaId);
            stmt.setInt(3, modalidadeId);
            stmt.setInt(4, eventoID);


            stmt.executeUpdate();

        } catch (SQLException | IOException e) {
            if (e.getMessage().contains("Esta inscrição já existe")) {
                AlertUtils.showErrorAlert("Erro", "Esta inscrição já existe!");
            } else {
                e.printStackTrace();
                throw new RuntimeException("Erro ao inscrever equipa: " + e.getMessage());
            }
        }
    }

    /**
     * Faz um inscrição pelos IDs.
     *
     * @param modalidadeId Modalidade id
     * @param eventoId Evento id
     * @param atletaId Atleta id
     * @throws SQLException erro se ja existe
     */

    public static void inscreverModalidadeByIds(int modalidadeId, int eventoId, int atletaId) {
        String query = "{call sp_inserir_inscricao(?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, atletaId);
            stmt.setNull(2, java.sql.Types.INTEGER);
            stmt.setInt(3, modalidadeId);
            stmt.setInt(4, eventoId);

            stmt.executeUpdate();
            AlertUtils.showInfoAlert("Sucesso","Atleta inscrito com sucesso!");
        } catch (SQLException | IOException e) {
            if (e.getMessage().contains("Esta inscrição já existe")) {
                AlertUtils.showErrorAlert("Erro", "Esta inscrição já existe!");
            } else {
                e.printStackTrace();
                throw new RuntimeException("Erro ao inscrever modalidade: " + e.getMessage());
            }
        }
    }
}