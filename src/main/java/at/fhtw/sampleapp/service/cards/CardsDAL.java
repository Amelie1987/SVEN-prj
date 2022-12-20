package at.fhtw.sampleapp.service.cards;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardsDAL {
    public CardsDAL() {
    }

    public String showCards(Token token) throws SQLException {  //creates packages when admin is logged in
        UnitOfWork unitOfWork = new UnitOfWork();
        Users dbUser = new Users();
        Cards cards[] = new Cards[100];   //max. of 100 cards
        String message ="200/ ";
        if(token.getToken_id() == null) {
            System.out.println("No token available");
            return "401";             // authentication information is missing or invalid
        }
        try {       // get user_id from username from token
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?;");
            preparedStatement.setString(1,token.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                dbUser.setId(resultSet.getInt(1));
            }
            preparedStatement.close();
            //System.out.println("User_id = " + dbUser.getId());
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ?");
            sqlStatement.setInt(1, dbUser.getId());
            ResultSet cardSet = sqlStatement.executeQuery();
           // unitOfWork.commitTransaction();
            int i = 0;
            while (cardSet.next()) {
                cards[i] = new Cards();
                cards[i].setCard_id(cardSet.getString( 1));
                message += cards[i].getCard_id() + " // ";
               // System.out.println("card_id " + cards[i].getCard_id());
                i++;
            }
            message+= "\n";
            if(cards[0].getCard_id() == null){
                return "204";
            }
            unitOfWork.finishWork();

        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        return message;
    }
}
