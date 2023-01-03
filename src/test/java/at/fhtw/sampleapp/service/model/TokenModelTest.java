package at.fhtw.sampleapp.service.model;
import at.fhtw.sampleapp.model.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenModelTest {

    //test if constructor sets username right and proove if its admin
    @Test
    void testTokenModelSetUsername() {
        Token token = new Token("Basic amelie-mtcgToken");
        assertEquals("amelie", token.getUsername());

    }

    //test if constructor sets username right and proove if its admin
    @Test
    void testTokenModelSetUsernameIsAdminTrue() {
        Token token = new Token("Basic admin-mtcgToken");
        assertEquals("admin", token.getUsername());
        assertTrue(token.isAdmin());
    }

    //test isAdmin()
    @Test
    void testTokenModelIsAdmin() {
        Token token = new Token("Basic amelie-mtcgToken");
        assertFalse(token.isAdmin());

    }
};
