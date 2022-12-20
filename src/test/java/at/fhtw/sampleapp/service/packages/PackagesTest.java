package at.fhtw.sampleapp.service.packages;

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
public class PackagesTest {

    //test if there is an error, when a card is double in a package
    @Test
    void testPackagesServiceInsertDoubleCard() throws Exception {
        URL url = new URL("http://localhost:10001/packages");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty ("Authorization", "Basic admin-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("[{\"Id\":\"70962948-2bf7-44a9-9ded-8c68eeac7793\", \"Name\":\"WaterGoblin\", \"Damage\":  9.0}, " +
                "{\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, " +
                "{\"Id\":\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\", \"Name\":\"Knight\", \"Damage\": 21.0}, " +
                "{\"Id\":\"a6fde738-c65a-4b10-b400-6fef0fdb28ba\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, " +
                "{\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55.0}]");
        outputStreamWriter.flush();
        outputStreamWriter.close();
        outputStream.close();
        urlConnection.connect();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            assertEquals(409, bufferedReader);
            urlConnection.getOutputStream().close();
            bufferedReader.close();
        } catch (IOException e){
            System.out.println("Exception: " + e);
        }
    }

    //test if there is bad request when no cards are send = no body set
    @Test
    void testPackagesServiceInsertNoCards() throws Exception {
        PackagesService packagesService = new PackagesService();
        URL url = new URL("http://localhost:10001/packages");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty ("Authorization", "Basic admin-mtcgToken");
        urlConnection.setRequestProperty("Method", "POST");
        urlConnection.connect();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            assertEquals(400, bufferedReader);          // BAD REQUEST
            urlConnection.getOutputStream().close();
            bufferedReader.close();
        } catch (IOException e){
            System.out.println("Exception: " + e);
        }
    }

    //test if there is an error, when it is not the admins token
    @Test
    void testPackagesServiceNoAdminToken() throws Exception {
        URL url = new URL("http://localhost:10001/packages");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty ("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("[{\"Id\":\"70962948-2bf7-44a9-9ded-8c68eeac7793\", \"Name\":\"WaterGoblin\", \"Damage\":  9.0}, " +
                "{\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, " +
                "{\"Id\":\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\", \"Name\":\"Knight\", \"Damage\": 21.0}, " +
                "{\"Id\":\"a6fde738-c65a-4b10-b400-6fef0fdb28ba\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, " +
                "{\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55.0}]");
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
        } catch (IOException e){
            System.out.println("Exception: " + e);
        }
    }

}
