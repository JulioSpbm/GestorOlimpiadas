package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.ModalidadeDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Modalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.RecordeModalidade;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.VencedorOlimpico;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.ModalidadeRecorde;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.OptionalInt;

class ModalidadeUATTest {

    @Test
    void testCreateModalidadeSuccess() {
        Modalidade modalidade = new Modalidade();
        modalidade.setMinParticipantes(5);
        modalidade.setNome("Teste Modalidade");
        modalidade.setDescricao("Descrição de Teste");
        modalidade.setNumJogos(10);
        modalidade.setGenero(true);
        modalidade.setTipo(false);
        modalidade.setIdPontuacao(1);
        modalidade.setRegras("Regras de Teste");

        assertTrue(ModalidadeDAO.createModalidade(modalidade));
    }

    @Test
    void testEditModalidadeSuccess() {
        Modalidade modalidade = new Modalidade();
        modalidade.setMinParticipantes(4);
        modalidade.setNome("Modalidade Editada");
        modalidade.setDescricao("Nova descrição");
        modalidade.setNumJogos(8);
        modalidade.setGenero(false);
        modalidade.setTipo(true);
        modalidade.setIdPontuacao(2);
        modalidade.setRegras("Novas Regras");
        modalidade.setEstado("Ativo");

        assertTrue(ModalidadeDAO.editModalidade(modalidade, 1));
    }

    @Test
    void testEditModalidadeFailure() {
        Modalidade modalidade = new Modalidade();
        modalidade.setNome("Modalidade Não Existente");

        assertFalse(ModalidadeDAO.editModalidade(modalidade, -1));
    }

    @Test
    void testGetPontuacaoIdByTipo() {
        String pontuacaoTipo = "Distance";
        String idPontuacao = ModalidadeDAO.getPontuacaoIdByTipo(pontuacaoTipo);
        assertNotNull(idPontuacao);
    }

    @Test
    void testGetScoreIdByName() {
        OptionalInt scoreId = ModalidadeDAO.getScoreIdByName("Time");
        assertTrue(scoreId.isPresent());
    }

    @Test
    void testListarModalidadesSuccess() {
        ModalidadeDAO modalidadeDAO = new ModalidadeDAO();
        List<Modalidade> modalidades = modalidadeDAO.listarModalidades();
        assertNotNull(modalidades);
        assertFalse(modalidades.isEmpty());
    }

    @Test
    void testListarModalidadesSemEventoSuccess() {
        ModalidadeDAO modalidadeDAO = new ModalidadeDAO();
        List<Modalidade> modalidades = modalidadeDAO.listarModalidadesSemEvento();
        assertNotNull(modalidades);
        assertFalse(modalidades.isEmpty());
    }

    @Test
    void testCreateModalidadeXML() {
        Modalidade modalidade = new Modalidade();
        modalidade.setTipoNome("Individual");
        modalidade.setNome("Teste XML");
        modalidade.setDescricao("Teste inserção XML");
        modalidade.setMinParticipantes(6);
        modalidade.setPontuacaoTipo("Points");
        modalidade.setNumJogosNome("One");
        modalidade.setRegras("Regras XML");
        modalidade.setGeneroNome("Women");

        assertDoesNotThrow(() -> ModalidadeDAO.createModalidadeXML(modalidade));
    }

    @Test
    void testCreateRecordOlimpico() {
        RecordeModalidade recorde = new RecordeModalidade("Atleta Teste", "Equipe Teste", 3, 9.58f, 2024);
        ModalidadeDAO dao = new ModalidadeDAO();
        assertDoesNotThrow(() -> dao.createRecordOlimpico(recorde));
    }

    @Test
    void testCreateVencedorOlimpico() {
        VencedorOlimpico vencedor = new VencedorOlimpico("Atleta Vencedor", "Equipe Vencedora", 1, 10.5f, 2024);
        ModalidadeDAO dao = new ModalidadeDAO();
        assertDoesNotThrow(() -> dao.createVencedorOlimpico(vencedor));
    }

    @Test
    void testGetScores() {
        assertNotNull(ModalidadeDAO.getScores());
    }

    @Test
    void testGetModalidadesComRecordes() {
        List<ModalidadeRecorde> records = ModalidadeDAO.getModalidadesComRecordes();
        assertNotNull(records);
    }
}
