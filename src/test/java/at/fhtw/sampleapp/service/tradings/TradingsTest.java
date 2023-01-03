package at.fhtw.sampleapp.service.tradings;


import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Trade;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


import static org.junit.jupiter.api.Assertions.assertEquals;

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

    //test POST /tradings/trade_id tradingDAL executeDeal response Code 403 user does not own this card
    @Test
    void testTradingsDALexecuteDeal() throws Exception {
        TradingsDAL tradingsDAL = new TradingsDAL();
        Token token = new Token();
        Trade trade = new Trade();
        token.setToken_id("Basic kienboec-mtcgToken");
        trade.setTrade_id("9bd85277-459f-49d4-b0cf-ba0a921faad0");
        trade.setMinDamage(20);
        trade.setCard_id("1d3f175b-c067-4359-989d-96562bfa382c");
        tradingsDAL.createDeals(token, trade);          // create deal
        URL url = new URL("http://localhost:10001/tradings/9bd85277-459f-49d4-b0cf-ba0a921faad0");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic altenhof-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("\"\\\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\\\"\"");
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

    //test POST /tradings/trade_id tradingDAL executeDeal response Code 403 - not min dmg
    @Test
    void testTradingsDALexecuteDealMinDmg() throws Exception {
        TradingsDAL tradingsDAL = new TradingsDAL();
        Token token = new Token();
        Trade trade = new Trade();
        token.setToken_id("Basic kienboec-mtcgToken");
        trade.setTrade_id("9bd85277-459f-49d4-b0cf-ba0a921faad0");
        trade.setMinDamage(60);
        trade.setCard_id("1d3f175b-c067-4359-989d-96562bfa382c");
        tradingsDAL.createDeals(token, trade);          // create deal
        URL url = new URL("http://localhost:10001/tradings/9bd85277-459f-49d4-b0cf-ba0a921faad0");
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic altenhof-mtcgToken");
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Method", "POST");
        // set body with outputstream
        OutputStream outputStream = urlConnection.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
        outputStreamWriter.write("\"\\\"4ec8b269-0dfa-4f97-809a-2c63fe2a0025\\\"\"");  //(=Ork, dmg 55)
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
}
