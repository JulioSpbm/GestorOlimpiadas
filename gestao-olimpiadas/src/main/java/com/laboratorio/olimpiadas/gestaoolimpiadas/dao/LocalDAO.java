package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de acesso a dados (DAO) para operações relacionadas com o Local na base de dados.
 */
public class LocalDAO {

    /**
     * Insere um novo local na base de dados.
     *
     * @param local Objeto Local que contém os dados a serem inseridos.
     * @return true se o local for inserido com sucesso; caso contrário, false.
     */
    public static boolean createLocal(Local local) {
        String sql = "{CALL sp_inserir_local(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, local.getNome());
            stmt.setString(2, local.getMorada());
            stmt.setString(3, local.getCidade());
            stmt.setInt(4, local.getCapacidade());
            stmt.setInt(5, local.getAnoConstrucao());

            stmt.setInt(6, local.getTipoLocal() ? 1 : 0);
            

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Edita um local na base de dados.
     *
     * @param local Objeto Local que contém os dados a serem inseridos.
     * @return true se o local for inserido com sucesso; caso contrário, false.
     */

    public static boolean updateLocal(Local local) {
        String sql = "{CALL sp_editar_local(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, local.getId());
            stmt.setString(2, local.getNome());
            stmt.setString(3, local.getMorada());
            stmt.setString(4, local.getCidade());
            stmt.setInt(5, local.getCapacidade());
            stmt.setInt(6, local.getAnoConstrucao());
            stmt.setInt(7, local.getTipoLocal() ? 1 : 0);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao editar local: " + e.getMessage());
            return false;
        }
    }


    public List<Local> listarLocais() {
        List<Local> locais = new ArrayList<>();
        String sql = "{CALL sp_buscar_local}";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Local local = new Local(
                        rs.getInt("id_local"),
                        rs.getString("nome"),
                        rs.getString("morada"),
                        rs.getString("cidade"),
                        rs.getInt("capacidade"),
                        rs.getInt("ano_construcao"),
                        rs.getBoolean("tipo_local")
                );
                locais.add(local);
            }
            return locais;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ObservableList<String> listarTipoLocais() {
        ObservableList<String> locais = FXCollections.observableArrayList();
        String sql = "{CALL sp_buscar_tipos_local}";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String local = rs.getString("tipo_local_desc");
                locais.add(local);
            }
            return locais;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

