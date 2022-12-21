package at.fhtw.sampleapp.service.model;
import at.fhtw.sampleapp.model.Token;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class TokenModelTest {

    //test if constructor sets username right and proove if its admin
    @Test
    void testTokenModelSetUsernameIsAdminFalse() {
        Token token = new Token("Basic amelie-mtcgToken");
        assertEquals("amelie", token.getUsername());
        assertFalse(token.isAdmin());
    }

    //test if constructor sets username right and proove if its admin
    @Test
    void testTokenModelSetUsernameIsAdminTrue() {
        Token token = new Token("Basic admin-mtcgToken");
        assertEquals("admin", token.getUsername());
        assertTrue(token.isAdmin());
    }
};
