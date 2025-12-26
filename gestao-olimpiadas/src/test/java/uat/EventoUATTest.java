package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.EventoDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class EventoUATTest {

    @Test
    void testCreateEventoSuccess() {
        Evento evento = new Evento();
        evento.setNome("Evento Teste");
        evento.setNome_mascote("Mascote Teste");
        evento.setDescricao_mascote("Descrição do Mascote");
        evento.setImagemMascote("imagem.jpg");
        evento.setData_inicio(Date.valueOf(LocalDate.of(2025, 5, 1)));
        evento.setData_fim(Date.valueOf(LocalDate.of(2025, 5, 10)));
        evento.setOlimpiada(new Olimpiada(1, 2024));

        EventoDAO eventoDAO = new EventoDAO();
        assertTrue(eventoDAO.createEvento(evento));
    }

    @Test
    void testGetAllCountriesSuccess() throws SQLException {
        EventoDAO eventoDAO = new EventoDAO();
        List<String> countries = eventoDAO.getAllCountries();
        assertNotNull(countries);
        assertFalse(countries.isEmpty());
    }

    @Test
    void testGetPaisIdByNameSuccess() {
        EventoDAO eventoDAO = new EventoDAO();
        int id = eventoDAO.getPaisIdByName("Portugal");
        assertNotEquals(-1, id);
    }

    @Test
    void testGetPaisIdByNameFailure() {
        EventoDAO eventoDAO = new EventoDAO();
        int id = eventoDAO.getPaisIdByName("País Inexistente");
        assertEquals(-1, id);
    }

    @Test
    void testListarEventosSuccess() {
        EventoDAO eventoDAO = new EventoDAO();
        List<Evento> eventos = eventoDAO.listarEventos();
        assertNotNull(eventos);
        assertFalse(eventos.isEmpty());
    }

    @Test
    void testUpdateEventoSuccess() {
        Evento evento = new Evento();
        evento.setId(1);
        evento.setNome("Evento Atualizado2");
        evento.setNome_mascote("Mascote Atualizado2");
        evento.setDescricao_mascote("Descrição atualizada");
        evento.setImagemMascote("imagem2.jpg");
        evento.setData_inicio(Date.valueOf(LocalDate.of(2025, 6, 1)));
        evento.setData_fim(Date.valueOf(LocalDate.of(2025, 6, 10)));
        evento.setOlimpiada(new Olimpiada(2024, 1));

        EventoDAO eventoDAO = new EventoDAO();
        assertTrue(eventoDAO.updateEvento(evento));
    }

    @Test
    void testUpdateEventoFailure() {
        Evento evento = new Evento();
        evento.setId(-1);
        evento.setNome("Evento Não Atualizado");

        EventoDAO eventoDAO = new EventoDAO();
        assertFalse(eventoDAO.updateEvento(evento));
    }

    @Test
    void testGetModalidadesByIdEventSuccess() {
        EventoDAO eventoDAO = new EventoDAO();
        List<Modalidade> modalidades = eventoDAO.getModalidadesByIdEvent(1);
        assertNotNull(modalidades);
        assertFalse(modalidades.isEmpty());
    }

    @Test
    void testGetEquipasByIdModalidadeEventoSuccess() {
        EventoDAO eventoDAO = new EventoDAO();
        List<Equipa> equipas = eventoDAO.getEquipasByIdModalidadeEvento(2, 1, "Masculino");
        assertNotNull(equipas);
        assertFalse(equipas.isEmpty());
    }

    @Test
    void testGetAtletasByIdModalidadeEventoSuccess() {
        EventoDAO eventoDAO = new EventoDAO();
        List<Atleta> atletas = eventoDAO.getAtletasByIdModalidadeEvento(1, 1, "Feminino");
        assertNotNull(atletas);
        assertFalse(atletas.isEmpty());
    }

    @Test
    void testGetEquipasByIdModalidadeEventoFailure() {
        EventoDAO eventoDAO = new EventoDAO();
        List<Equipa> equipas = eventoDAO.getEquipasByIdModalidadeEvento(1, 1, "Masculino");
        assertTrue(equipas.isEmpty());
    }

    @Test
    void testGetAtletasByIdModalidadeEventoFailure() {
        EventoDAO eventoDAO = new EventoDAO();
        List<Atleta> atletas = eventoDAO.getAtletasByIdModalidadeEvento(2, 1, "Feminino");
        assertTrue(atletas.isEmpty());
    }
}
