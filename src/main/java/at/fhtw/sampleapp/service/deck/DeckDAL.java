package at.fhtw.sampleapp.service.deck;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckDAL {

    public DeckDAL() {
    }

    public String showDeckBeautiful(Token token) throws SQLException {  //creates packages when admin is logged in
        UnitOfWork unitOfWork = new UnitOfWork();
        Users dbUser = new Users();
        Cards cards[] = new Cards[10];
        // List cards = new List();
        String message = "200/ ";      //response at success
        int deck_id = 0;
        dbUser.setUsername(token.getUsername());
        if(token.getToken_id() == null) {
            return "401";             // authentication information is missing or invalid
        }

        try {
            // get user_id
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, dbUser.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            if(resultSet.next() == false){
                return "401";
            } else {
                dbUser.setId(resultSet.getInt(1));
            }
            System.out.println("user_id = " + dbUser.getId());
            sqlStatement.close();
            // get deck from user
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "SELECT deck_id FROM deck WHERE user_id = ?");
            preparedStatement.setInt(1, dbUser.getId());
            ResultSet deckSet = preparedStatement.executeQuery();
            while(deckSet.next()){
                deck_id = deckSet.getInt(1);
            }
            preparedStatement.close();
            System.out.println("Deck_id = " + deck_id);
            if(deck_id == 0){         // response request is fine, but deck is empty
                System.out.println("Error no deck");
                unitOfWork.finishWork();
                return "404";
            }
            //get cards from user
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM deck_cards WHERE deck_id = ?");
            prepStatement.setInt(1,deck_id);
            ResultSet cardSet = prepStatement.executeQuery();
            int i = 0;
            while (cardSet.next()) {
                cards[i] = new Cards();
                cards[i].setCard_id(cardSet.getString( 1));
                //message += cards[i].getCard_id() + " // ";
                System.out.println("card_id " + cards[i].getCard_id());
                i++;
            }
            prepStatement.close();
          //  unitOfWork.finishWork();

        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try {

        //get card infos from cards table
        PreparedStatement prepStatement = unitOfWork.prepareStatement("");
        for(int i = 0; i < 4; i++) {
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT name,damage FROM cards WHERE card_id = ?");
            prepStatement.setString(1, cards[i].getCard_id());
            ResultSet cardSet = prepStatement.executeQuery();
            //int j = 0;
            while (cardSet.next()) {
                cards[i].setName(cardSet.getString(1));
                cards[i].setDamage(cardSet.getInt(2));
                message += "\n\n Card ID: " + cards[i].getCard_id() + "\n Name: " + cards[i].getName() + "\n Damage: " + cards[i].getDamage();
                System.out.println("card_id " + cards[i].getCard_id());
            }
        }
        prepStatement.close();
        unitOfWork.finishWork();

    } catch (SQLException e) {
        System.out.println("SQL SELECT Exception: " + e.getMessage());

        unitOfWork.finishWork();
    }
        return message;
    }

    public String showDeck(Token token) throws SQLException {  //creates packages when admin is logged in
        UnitOfWork unitOfWork = new UnitOfWork();
        Users dbUser = new Users();
        Cards cards[] = new Cards[10];
       // List cards = new List();
        String message = "200/ ";      //response at success
        int deck_id = 0;
        dbUser.setUsername(token.getUsername());
        System.out.println("showDeck - User: " + dbUser.getUsername());
        if(token.getToken_id() == null) {
            return "401";             // authentication information is missing or invalid
        }

        try {
            // get user_id
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, dbUser.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            while(resultSet.next()){
                dbUser.setId(resultSet.getInt(1));
            }
            System.out.println("user_id = " + dbUser.getId());
            sqlStatement.close();
            // get deck from user
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "SELECT deck_id FROM deck WHERE user_id = ?");
            preparedStatement.setInt(1, dbUser.getId());
            ResultSet deckSet = preparedStatement.executeQuery();
            while(deckSet.next()){
                deck_id = deckSet.getInt(1);
            }
            preparedStatement.close();
            System.out.println("Deck_id = " + deck_id);
            if(deck_id == 0){         // response request is fine, but deck is empty
                System.out.println("Error no deck");
                unitOfWork.finishWork();
                return "404";
            }
            //get cards from user
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM deck_cards WHERE deck_id = ?");
            prepStatement.setInt(1,deck_id);
            ResultSet cardSet = prepStatement.executeQuery();
            int i = 0;
            while (cardSet.next()) {
                cards[i] = new Cards();
                cards[i].setCard_id(cardSet.getString( 1));
                message += cards[i].getCard_id() + " // ";
                System.out.println("card_id " + cards[i].getCard_id());
                i++;
            }
            prepStatement.close();
            unitOfWork.finishWork();

        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        return message;
    }

    public Integer configureDeck(String cardString, Token token) throws SQLException {  //creates packages when admin is logged in
        UnitOfWork unitOfWork = new UnitOfWork();
        Users dbUser = new Users();
        Cards cards[] = new Cards[10];
        int deck_id = 0;
        dbUser.setUsername(token.getUsername());
        System.out.println("Username: " + dbUser.getUsername());
        if(token.getToken_id() == null) {
            return 401;             // authentication information is missing or invalid
        }
        // card String to card obj with setId()
        String tmpString[] = cardString.split("\"", 2); // get rid of [
        String tmp[];
        //System.out.println("Card array length: " + cards.length);
        for(int i = 0; i < cards.length; i++){
            cards[i] = new Cards();
            tmp = tmpString[1].split("\"", 2);
            cards[i].setCard_id(tmp[0]);
            //  cards.add(new Cards());
           // System.out.print("1st output TmpStr[0]: " + tmpString[0] + "TmpStr[1]: " + tmpString[1]);
            System.out.println("Card " + i + " ID: " + cards[i].getCard_id());
            if(tmpString[1].equals(cards[i].getCard_id() + "\"]")){
                break;
            }
           // System.out.println("IF condition: " + cards[i].getCard_id() + "\"]");
            tmpString = tmp[1].split("\"", 2);          // get rid of ","
           // System.out.print("2nd output TmpStr[0]: " + tmpString[0] + "TmpStr[1]: " + tmpString[1]);
        }
        if(cards[3] == null){
            System.out.println("There are not enough cards.");
            return 400;
        }
        /*
        try {
            System.out.println("Card 4 is Set? " + cards[3]);
        } catch(Exception e) {
            System.out.println("There are not enough cards. "+ e);
        }
        System.out.println("Card 4 is Set? " + cards[3]);
        */
        try {
            // get user_id
            System.out.println("Username: " + dbUser.getUsername());
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?;");
            preparedStatement.setString(1, dbUser.getUsername());
            ResultSet rSet = preparedStatement.executeQuery();
            //unitOfWork.commitTransaction();
            while (rSet.next()) {
                dbUser.setId(rSet.getInt(1));
            }
            System.out.println("user_id in select id for creating deck = " + dbUser.getId());
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try{
            // proof if cards are available - in users stack
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ? " +
                            "AND (card_id = ? OR card_id = ? OR card_id = ? OR card_id = ?)");
            sqlStatement.setInt(1, dbUser.getId());
            for(int j = 0; j < 4; j++) {
                sqlStatement.setString(j+2, cards[j].getCard_id());
            }
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {
                System.out.println("One card is not in the users stack");
                return 403;
            }
            //unitOfWork.commitTransaction();

            System.out.println("ConfigureDeck user_id  = " + dbUser.getId());
            sqlStatement.close();
        } catch (SQLException e) {
                System.out.println("SQL SELECT Exception: " + e.getMessage());
                unitOfWork.rollbackTransaction();
                unitOfWork.finishWork();
        }
        try {
            // insert deck into table deck and get deck_id
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "INSERT INTO deck (user_id) VALUES (?)");
            prepStatement.setInt(1, dbUser.getId());
            prepStatement.executeUpdate();
            unitOfWork.commitTransaction();
            prepStatement.close();

            PreparedStatement deckStatement = unitOfWork.prepareStatement(
                    "SELECT deck_id FROM deck WHERE user_id = ?");
            deckStatement.setInt(1, dbUser.getId());
            ResultSet deckSet = deckStatement.executeQuery();
            while(deckSet.next()){
                deck_id = deckSet.getInt(1);
            }
            deckStatement.close();
            System.out.println("Deck_id = " + deck_id);
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try {
         /*   if(deck_id == 0){         // response request is fine, but deck is empty
                System.out.println("Error no deck");
                unitOfWork.finishWork();
                return 404;
            } */
            //insert cards into the users deck
            PreparedStatement insertStatement;
            for(int i = 0; i < 4; i++){
                insertStatement = unitOfWork.prepareStatement(
                        "INSERT INTO deck_cards (deck_id, card_id) VALUES (?,?)");
                insertStatement.setInt(1, deck_id);
                insertStatement.setString(2, cards[i].getCard_id());
                insertStatement.executeUpdate();
                unitOfWork.commitTransaction();
            }
            unitOfWork.finishWork();

        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        return 200;
    }

}
