package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.UtilizadorDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.sql.SQLException;

class UtilizadorUATTest {

    @Test
    void testIsSaltUnique() throws SQLException {
        String salt = "randomSalt123";
        boolean isUnique = UtilizadorDAO.isSaltUnique(salt);
        assertNotNull(isUnique);
    }

    @Test
    void testGenerateUniqueSalt() throws SQLException {
        String salt = UtilizadorDAO.generateUniqueSalt();
        assertNotNull(salt);
        assertFalse(salt.isEmpty());
    }

    @Test
    void testGetLastNumMecanografico() throws SQLException, IOException {
        String numMecanografico = UtilizadorDAO.getLastNumMecanografico(1);
        assertNotNull(numMecanografico);
    }

    @Test
    void testGetNomeUserByNumMecan() throws SQLException {
        String nome = UtilizadorDAO.getNomeUserByNumMecan("A5000068");
        assertNotNull(nome);
    }

    @Test
    void testGetIdUserByNumMecanografico() throws SQLException, IOException {
        int id = UtilizadorDAO.getIdUserByNumMecanografico("A5000068");
        assertTrue(id >= 0);
    }

    @Test
    void testGetFotoUserByNumMecanografico() throws SQLException, IOException {
        String foto = UtilizadorDAO.getFotoUserByNumMecanografico("A5000068");
        assertNotNull(foto);
    }

    @Test
    void testSetFotoUserByNumMecanografico() throws SQLException {
        boolean updated = UtilizadorDAO.setFotoUserByNumMecanografico("A5000068", "base64ImageString");
        assertTrue(updated);
    }

    @Test
    void testGetNomeUserById() throws SQLException {
        String nome = UtilizadorDAO.getNomeUserById(2);
        assertNotNull(nome);
    }
}