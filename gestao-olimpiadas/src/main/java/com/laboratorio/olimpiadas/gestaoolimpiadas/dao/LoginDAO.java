package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.PasswordUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por validar utilizadores na aplicação.
 * Contém métodos para validar credenciais e gerar hash de palavra-passe com salt.
 */
public class LoginDAO {

    /**
     * Valida um utilizador verificando o nome e palavra-passe fornecidos.
     * A palavra-passe é comparada com o hash armazenado na base de dados.
     * @param num_mecanografico o nome do utilizador a ser validado.
     * @param password a palavra-passe do utilizador a ser validada.
     * @return o tipo de utilizador (id_tipo_utilizador) se a autenticação for bem-sucedida; {@code null} caso contrário.
     * @throws SQLException se ocorrer um erro de acesso à base de dados.
     */
    public static Integer authenticate(String num_mecanografico, String password) throws SQLException {
        String sql = "SELECT senha, salt, id_tipo_utilizador FROM tab_utilizador WHERE num_mecanografico = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, num_mecanografico);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String PasswordSavedHash = resultSet.getString("senha");
                String SaltStored = resultSet.getString("salt");
                int userTypeId = resultSet.getInt("id_tipo_utilizador");

                if (password.equals(num_mecanografico)) {
                    return -1;
                }

                String PasswordHash = PasswordUtils.hashPassword(password, SaltStored);
                if (PasswordSavedHash.equals(PasswordHash)) {
                    return userTypeId;
                }
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Atualiza a palavra passe atraves do numero mecanografico.
     * @param numMecanografico
     * @param newPassword
     * @return
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static boolean updatePassword(String numMecanografico, String newPassword) throws SQLException, NoSuchAlgorithmException, IOException {
        String sql = "UPDATE tab_utilizador SET senha = ?, salt = ? WHERE num_mecanografico = ?";

        String newSalt = PasswordUtils.generateSalt(); // Gerar novo salt
        String hashedPassword = PasswordUtils.hashPassword(newPassword, newSalt); // Hash da nova senha

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, hashedPassword);
            statement.setString(2, newSalt);
            statement.setString(3, numMecanografico);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    /**
     * Verifica a senha inserida com a senha da base de dados.
     * @param numMecanografico
     * @param password
     * @return
     * @throws SQLException
     */
    public static boolean checkCurrentPassword(String numMecanografico, String password) throws SQLException {
        String sql = "SELECT senha FROM tab_utilizador WHERE num_mecanografico = ?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numMecanografico);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("senha");

                return storedPassword.equals(password);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * Recupera a palavra passe e o salt.
     * @param numMecanografico
     * @return
     * @throws SQLException
     */
    public static Map<String, String> getPasswordAndSaltByNumMecanografico(String numMecanografico) throws SQLException, IOException {
        String sql = "SELECT senha, salt FROM tab_utilizador WHERE num_mecanografico = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numMecanografico);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Map<String, String> passwordAndSalt = new HashMap<>();
                passwordAndSalt.put("hash", rs.getString("senha"));
                passwordAndSalt.put("salt", rs.getString("salt"));
                return passwordAndSalt;
            }

            return null;
        }
    }

    /**
     * Faz a verificação da password.
     *
     * @param numMecanografico
     * @param plainPassword
     * @return
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     */
    public static boolean checkPassword(String numMecanografico, String plainPassword) throws SQLException, NoSuchAlgorithmException, IOException {
        Map<String, String> credentials = getPasswordAndSaltByNumMecanografico(numMecanografico);
        if (credentials != null) {
            String storedHash = credentials.get("hash");
            String storedSalt = credentials.get("salt");

            String recalculatedHash = PasswordUtils.hashPassword(plainPassword, storedSalt);

            return storedHash.equals(recalculatedHash);
        }

        return false;
    }
}