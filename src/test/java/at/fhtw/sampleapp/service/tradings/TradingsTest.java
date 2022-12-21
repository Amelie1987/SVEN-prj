package at.fhtw.sampleapp.service.tradings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class TradingsTest {

    //test GET /tradings createDeal response Code 401 (token invalid)
    @Test
    void testTradingsServiceDealInvalidToken() throws Exception {
        URL url = new URL("http://localhost:10001/tradings");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic amelie-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", " +
                "\"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": " +
                "\"Monster\", \"MinimumDamage\": 30}");
        outputStreamWriter.flush();
        outputStreamWriter.close();
        outputStream.close();
        urlConnection.connect();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            assertEquals(401, bufferedReader);
            urlConnection.getOutputStream().close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }


    //test GET /tradings createDeal response Code 403 (forbidden - user does not own this card)
    @Test
    void testTradingsServiceUserDoesNotOwnTheCard() throws Exception {
        URL url = new URL("http://localhost:10001/tradings");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic admin-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", " +
                "\"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": " +
                "\"Monster\", \"MinimumDamage\": 30}");
        outputStreamWriter.flush();
        outputStreamWriter.close();
        outputStream.close();
        urlConnection.connect();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            assertEquals(403, bufferedReader);
            urlConnection.getOutputStream().close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    //test GET /tradings createDeal response Code 200 (success)
    @Test
    void testTradingsServiceValidDeal() throws Exception {
        URL url = new URL("http://localhost:10001/tradings");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", " +
                "\"CardToTrade\": \"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Type\": " +
                "\"Monster\", \"MinimumDamage\": 15}");
        outputStreamWriter.flush();
        outputStreamWriter.close();
        outputStream.close();
        urlConnection.connect();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            assertEquals(200, bufferedReader);
            urlConnection.getOutputStream().close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }
}
