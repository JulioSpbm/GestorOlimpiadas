package com.laboratorio.olimpiadas.gestaoolimpiadas.dao;

import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.XML;
import com.laboratorio.olimpiadas.gestaoolimpiadas.service.UtilizadorService;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.AlertUtils;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class XMLImporterDAO {
    /**
     * Recupera os históricos de importação de arquivos XML a partir da base de dados.
     * @return
     */
    public List<XML> listarHist() {
        List<XML> hist = new ArrayList<>();
        String sql = "SELECT data_importacao,id_utilizador,nome_ficheiro FROM tab_historico_xml";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                XML xml = new XML();
                xml.setData(String.valueOf(rs.getDate("data_importacao")));
                xml.setId_utilizador(rs.getInt("id_utilizador"));
                xml.setFicheiro(rs.getString("nome_ficheiro"));

                hist.add(xml);
            }
            return hist;
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Regista um novo histórico de importação XML.
     *
     * @param id_utilizador
     * @param ficheiro
     */
    public void createHistoricoXML(int id_utilizador, String ficheiro) {
        String sql = "{CALL sp_inserir_historico_xml(?, ?)}";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id_utilizador);
            stmt.setString(2, ficheiro);

            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            AlertUtils.showErrorAlert("Erro!", "Erro ao inserir historico xml: " + e.getMessage());
        }
    }
}
