package uat;

import com.laboratorio.olimpiadas.gestaoolimpiadas.dao.LoginDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginUATTest {

    @Test
    void testLoginAdminSuccess() throws Exception {
        assertNotEquals(0, LoginDAO.authenticate("G1000001", "admin1"));
    }

    @Test
    void testLoginAtletaSuccess() throws Exception {
        assertNotEquals(0, LoginDAO.authenticate("A5000001", "atleta1"));
    }

    @Test
    void testLoginInvalidUsername() throws Exception {
        assertNull(LoginDAO.authenticate("user", "atleta1"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        assertNull(LoginDAO.authenticate("A5000001", "wrongPassword"));
    }
}
