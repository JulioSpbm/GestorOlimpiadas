package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Inscricao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscricaoDAO {

    /**
     * Lista todas as incrições.
     * @return
     */
    public List<Inscricao> listarInscricoes() {
        List<Inscricao> inscricoes = new ArrayList<>();
        String sql = "{CALL sp_buscar_Inscricao}";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inscricao inscricao = new Inscricao(
                        rs.getInt("id_inscricao"),
                        rs.getString("utilizador"),
                        rs.getString("equipa"),
                        rs.getString("modalidade"),
                        rs.getString("evento"),
                        rs.getString("estado")
                );
                inscricoes.add(inscricao);
            }
            return inscricoes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Atualiza o estado de uma inscrição.
     *
     * @param id
     * @param estado
     * @return
     */
    public static boolean atualizarEstado(int id, int estado) {
        String sql = "UPDATE tab_inscricao SET estado = ? WHERE id_inscricao = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, estado); 
            stmt.setInt(2, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao editar estado: " + e.getMessage());
            return false;
        }
    }
}
