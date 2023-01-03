package at.fhtw.sampleapp.service.battle;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BattleControllerTest {
    // test request without token
    @Test
    void testBattleControllerRequestWithoutValidToken() throws Exception {
        URL url = new URL("http://localhost:10001/battles");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty ("Authorization", "");
        urlConnection.setRequestProperty("Method", "POST");
        urlConnection.connect();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            assertEquals(401, bufferedReader);
            bufferedReader.close();
        } catch (IOException e){
            System.out.println("Exception: " + e);
        }
    }

}
