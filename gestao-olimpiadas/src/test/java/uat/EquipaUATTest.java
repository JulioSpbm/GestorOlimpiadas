package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.EquipaDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Equipa;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.EquipaRecorde;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Participacao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class EquipaUATTest {

    @Test
    void testCreateEquipaSuccess(){
        Equipa equipa = new Equipa();
        equipa.setPais(1);
        equipa.setidModalidade(2);
        equipa.setGenero(true);
        equipa.setAnoFundacao(2000);
        equipa.setNome("Equipa Teste");

        EquipaDAO equipaDAO = new EquipaDAO();
        assertTrue(equipaDAO.createEquipa(equipa));
    }

    @Test
    void testCreateEquipaXMLSuccess() {
        Equipa equipa = new Equipa();
        equipa.setNome("Equipa Teste XML");
        equipa.setPaisNome("PTR");
        equipa.setGeneroNome("Men");
        equipa.setModalidadeNome("Futebol");
        equipa.setAnoFundacao(1990);

        EquipaDAO equipaDAO = new EquipaDAO();
        assertDoesNotThrow(() -> equipaDAO.createEquipaXML(equipa));
    }

    @Test
    void testCreateParticipacaoTeamSuccess() {
        Participacao participacao = new Participacao(2024, 1, 0, 0);
        EquipaDAO equipaDAO = new EquipaDAO();
        assertDoesNotThrow(() -> equipaDAO.createParticipacaoTeam(participacao));
    }

    @Test
    void testEditEquipaSuccess() {
        Equipa equipa = new Equipa();
        equipa.setId(1);
        equipa.setPais(1);
        equipa.setidModalidade(2);
        equipa.setGenero(true);
        equipa.setAnoFundacao(1995);
        equipa.setNome("Equipa Atualizada");

        EquipaDAO equipaDAO = new EquipaDAO();
        assertTrue(equipaDAO.editEquipa(equipa));
    }

    @Test
    void testEditEquipaFailure() {
        Equipa equipa = new Equipa();
        equipa.setId(-1);
        equipa.setPais(1);
        equipa.setidModalidade(2);
        equipa.setGenero(true);
        equipa.setAnoFundacao(1995);
        equipa.setNome("Equipa Inexistente");

        EquipaDAO equipaDAO = new EquipaDAO();
        assertFalse(equipaDAO.editEquipa(equipa));
    }

    @Test
    void testGetAllCountriesSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        List<String> countries = equipaDAO.getAllCountries();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    void testGetPaisIdByNameSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        int id = equipaDAO.getPaisIdByName("Portugal");
        assertNotEquals(-1, id);
    }

    @Test
    void testGetPaisIdByNameFailure() {
        EquipaDAO equipaDAO = new EquipaDAO();
        int id = equipaDAO.getPaisIdByName("País Inexistente");
        assertEquals(-1, id);
    }

    @Test
    void testGetAllModalidadesSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        List<String> modalidades = equipaDAO.getAllModalidades();
        assertNotNull(modalidades);
        assertFalse(modalidades.isEmpty());
    }

    @Test
    void testGetModalidadeIdByNameSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        int id = equipaDAO.getModalidadeIdByName("Futebol");
        assertNotEquals(-1, id);
    }

    @Test
    void testGetModalidadeIdByNameFailure() {
        EquipaDAO equipaDAO = new EquipaDAO();
        int id = equipaDAO.getModalidadeIdByName("Modalidade Inexistente");
        assertEquals(-1, id);
    }

    @Test
    void testListarEquipasSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        List<Equipa> equipas = equipaDAO.listarEquipas();
        assertNotNull(equipas);
        assertFalse(equipas.isEmpty());
    }

    @Test
    void testGetIdEquipaByNameSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        int id = equipaDAO.getIdEquipaByName("Equipa Teste");
        assertNotEquals(-1, id);
    }

    @Test
    void testGetIdEquipaByNameFailure() {
        EquipaDAO equipaDAO = new EquipaDAO();
        int id = equipaDAO.getIdEquipaByName("Equipa Inexistente");
        assertEquals(-1, id);
    }

    @Test
    void testGetTeamComRecordesSuccess() {
        List<EquipaRecorde> records = EquipaDAO.getTeamComRecordes();
        assertNotNull(records);
        assertFalse(records.isEmpty());
    }

    @Test
    void testListarEquipasFiltradasSuccess() {
        EquipaDAO equipaDAO = new EquipaDAO();
        List<Equipa> equipas = equipaDAO.listarEquipasFiltradas(2);
        assertNotNull(equipas);
        assertFalse(equipas.isEmpty());
    }
}
