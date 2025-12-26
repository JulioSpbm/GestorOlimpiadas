package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.LocalDAO;
import com.laboratorio.olimpiadas.gestaoolimpiadas.model.Local;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class LocalUATTest {

    @Test
    void testCreateLocalSuccess() {
        Local local = new Local();
        local.setNome("Local Teste");
        local.setMorada("Rua Teste, 123");
        local.setCidade("Cidade Teste");
        local.setCapacidade(500);
        local.setAnoConstrucao(2020);
        local.setTipoLocal(true);

        assertTrue(LocalDAO.createLocal(local));
    }

    @Test
    void testCreateLocalFailure() {
        Local local = new Local();
        local.setNome(null);
        local.setMorada("Rua Teste, 123");
        local.setCidade("Cidade Teste");
        local.setCapacidade(500);
        local.setAnoConstrucao(2020);
        local.setTipoLocal(true);

        assertFalse(LocalDAO.createLocal(local));
    }

    @Test
    void testUpdateLocalSuccess() {
        Local local = new Local();
        local.setId(1);
        local.setNome("Local Atualizado");
        local.setMorada("Rua Atualizada, 456");
        local.setCidade("Cidade Atualizada");
        local.setCapacidade(600);
        local.setAnoConstrucao(2021);

        assertTrue(LocalDAO.updateLocal(local));
    }

    @Test
    void testUpdateLocalFailure() {
        Local local = new Local();
        local.setId(-1);
        local.setNome("Local Não Atualizado");
        local.setMorada("Rua Não Atualizada, 789");
        local.setCidade("Cidade Não Atualizada");
        local.setCapacidade(0);
        local.setAnoConstrucao(2021);

        assertFalse(LocalDAO.updateLocal(local));
    }

    @Test
    void testListarLocaisSuccess() {
        LocalDAO localDAO = new LocalDAO();
        List<Local> locais = localDAO.listarLocais();
        assertNotNull(locais);
        assertFalse(locais.isEmpty());
    }

    @Test
    void testListarTipoLocaisSuccess() {
        LocalDAO localDAO = new LocalDAO();
        var tiposLocais = localDAO.listarTipoLocais();
        assertNotNull(tiposLocais);
        assertFalse(tiposLocais.isEmpty());
    }
}
