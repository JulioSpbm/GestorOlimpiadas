package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.AtletaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Atleta;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.AtletaRecorde;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Utilizador;
import com.laboratorio.olimpiadas.gestaoolimpiadas.utils.GenMecanografico;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AtletaUATTest {

    @Test
    void testCreateAtletaSuccess() throws SQLException, IOException {
        Atleta atleta = new Atleta();
        atleta.setidPais(1);
        atleta.setNome("Teste Atleta");
        atleta.setDataNascimento(Date.valueOf(LocalDate.of(2000, 1, 1)));
        atleta.setGenero(true);
        atleta.setAltura(180);
        atleta.setPeso(75);
        String username = GenMecanografico.generateNextMecanografico(2);
        Utilizador utilizador = new Utilizador(username, username, 2);
        atleta.setUtilizador(utilizador);
        AtletaDAO atletaDAO = new AtletaDAO();
        assertTrue(atletaDAO.createAtleta(atleta));
    }

    @Test
    void testCreateParticipacaoSuccess() {
        Participacao participacao = new Participacao(2024, 1, 1, 1);
        AtletaDAO atletaDAO = new AtletaDAO();
        assertDoesNotThrow(() -> atletaDAO.createParticipacao(participacao));
    }

    @Test
    void testGetAllCountriesSuccess() throws SQLException {
        AtletaDAO atletaDAO = new AtletaDAO();
        List<String> countries = atletaDAO.getAllCountries();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    void testGetPaisIdByNameSuccess() {
        AtletaDAO atletaDAO = new AtletaDAO();
        int id = atletaDAO.getPaisIdByName("Portugal");
        assertNotEquals(-1, id);
    }

    @Test
    void testGetPaisIdByNameFailure() {
        AtletaDAO atletaDAO = new AtletaDAO();
        int id = atletaDAO.getPaisIdByName("País Inexistente");
        assertEquals(-1, id);
    }

    @Test
    void testGetPaisIdByIsoNameSuccess() {
        AtletaDAO atletaDAO = new AtletaDAO();
        int id = atletaDAO.getPaisIdByIsoName("PRT");
        assertNotEquals(-1, id);
    }

    @Test
    void testGetPaisIdByIsoNameFailure() {
        AtletaDAO atletaDAO = new AtletaDAO();
        int id = atletaDAO.getPaisIdByIsoName("XXX");
        assertEquals(-1, id);
    }

    @Test
    void testListarAtletasSuccess() {
        AtletaDAO atletaDAO = new AtletaDAO();
        List<Atleta> atletas = atletaDAO.listarAtletas();
        assertNotNull(atletas);
        assertFalse(atletas.isEmpty());
    }



    @Test
    void testUpdateAtletaSuccess() {
        Atleta atleta = new Atleta();
        atleta.setId(2);
        atleta.setidPais(1);
        atleta.setNome("Nome Atualizado");
        atleta.setDataNascimento(Date.valueOf(LocalDate.of(2000, 1, 1)));
        atleta.setGenero(true);
        atleta.setAltura(175);
        atleta.setPeso(70);
        atleta.setEstado("Ativado");

        AtletaDAO atletaDAO = new AtletaDAO();
        assertTrue(atletaDAO.updateAtleta(atleta));
    }

    @Test
    void testUpdateAtletaFailure() {
        Atleta atleta = new Atleta();
        atleta.setId(-1);
        atleta.setDataNascimento(Date.valueOf(LocalDate.of(2000, 1, 1)));
        atleta.setEstado("Ativado");

        AtletaDAO atletaDAO = new AtletaDAO();
        assertFalse(atletaDAO.updateAtleta(atleta));
    }

    @Test
    void testGetAtletaByNumMecanograficoSuccess() {
        AtletaDAO atletaDAO = new AtletaDAO();
        Atleta atleta = atletaDAO.getAtletaByNumMecanografico("A5000001");
        assertNotNull(atleta);
    }

    @Test
    void testGetAtletaByNumMecanograficoFailure() {
        AtletaDAO atletaDAO = new AtletaDAO();
        Atleta atleta = atletaDAO.getAtletaByNumMecanografico("XXXXXXX");
        assertNull(atleta);
    }

    @Test
    void testGetAthleteComRecordesSuccess() {
        List<AtletaRecorde> records = AtletaDAO.getAthleteComRecordes();
        assertNotNull(records);
    }
}