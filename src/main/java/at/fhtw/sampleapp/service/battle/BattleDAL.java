package at.fhtw.sampleapp.service.battle;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleDAL {

    public BattleDAL(){

    }
    // battleDAL not needed because of repository usage!!!
    public String startBattle(Token token) throws SQLException {  //creates packages when admin is logged in
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
}
