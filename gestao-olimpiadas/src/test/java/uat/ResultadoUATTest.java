package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.ResultadoDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Resultado;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

class ResultadoUATTest {

    @Test
    void testGetIdMedalhaByNameSuccess() {
        int idMedalha = ResultadoDAO.getIdMedalhaByName("Gold");
        assertTrue(idMedalha > 0);
    }

    @Test
    void testGetIdMedalhaByNameFailure() {
        int idMedalha = ResultadoDAO.getIdMedalhaByName("Medalha Inexistente");
        assertEquals(-1, idMedalha);
    }

    @Test
    void testGetAllModalidadeInscricoesSuccess() throws SQLException {
        ResultadoDAO resultadoDAO = new ResultadoDAO();
        List<Resultado> resultados = resultadoDAO.getAllModalidadeInscricoes();
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }

    @Test
    void testSaveResultadoIndividualSuccess() {
        Resultado resultado = new Resultado();
        resultado.setIdModalidade(1);
        resultado.setIdAtleta(2);
        resultado.setIdMedalha(1);
        resultado.setDataResultado("2025-01-31");
        resultado.setPontuacaoIndividual(9.5);
        resultado.setIdEvento(2);

        ResultadoDAO resultadoDAO = new ResultadoDAO();
        assertTrue(resultadoDAO.saveResultadoIndividual(resultado));
    }

    @Test
    void testSaveResultadoEquipaSuccess() {
        Resultado resultado = new Resultado();
        resultado.setIdModalidade(1);
        resultado.setIdEquipa(1);
        resultado.setIdMedalha(1);
        resultado.setDataResultado("2025-01-31");
        resultado.setPontuacaoTeam(18.7);
        resultado.setIdEvento(1);

        ResultadoDAO resultadoDAO = new ResultadoDAO();
        assertTrue(resultadoDAO.saveResultadoEquipa(resultado));
    }

    @Test
    void testGetResultadoByModalidadeSuccess() {
        ResultadoDAO resultadoDAO = new ResultadoDAO();
        List<Resultado> resultados = resultadoDAO.getResultadoByModalidade(1);
        assertNotNull(resultados);
        assertFalse(resultados.isEmpty());
    }
}