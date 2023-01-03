package at.fhtw.sampleapp.dal.repository;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardRepository {
    private UnitOfWork unitOfWork;

    public CardRepository(UnitOfWork unitOfWork) { this.unitOfWork = unitOfWork; }

    //set card stats
    public void setCardStats(Cards card){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT name, damage FROM cards WHERE card_id = ?");
            sqlStatement.setString(1, card.getCard_id());
            ResultSet resultSet = sqlStatement.executeQuery();
            while(resultSet.next()){
                card.setName(resultSet.getString(1));
                card.setDamage(resultSet.getInt(2));
            }
            unitOfWork.commitTransaction();
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }
    //get random card from deck id
    public Boolean getRandomCard(Users user, Cards card){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM deck_cards WHERE deck_id = ? ORDER BY RANDOM() LIMIT 1");
            sqlStatement.setInt(1, user.getDeck_id());
            ResultSet resultSet = sqlStatement.executeQuery();
            if(resultSet.next() == false){
                user.setLost(true);
                System.out.println("User " + user.getId() + " has no cards left.");
                sqlStatement.close();
                return false;
            } else {
                card.setCard_id(resultSet.getString(1));
                sqlStatement.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return false;
    }
    //get random card from users_stack
    public Boolean getRandomCardFromStack(Users user, Cards card){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ? ORDER BY RANDOM() LIMIT 1");
            sqlStatement.setInt(1, user.getId());
            ResultSet resultSet = sqlStatement.executeQuery();
            if(resultSet.next() == false){
                user.setLost(true);
                System.out.println("User " + user.getId() + "has no cards in his stack left.");
                sqlStatement.close();
                return false;
            } else {
                card.setCard_id(resultSet.getString(1));
                sqlStatement.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return false;
    }
    //get random card from users_stack true if exists, false if not
    public Boolean proofIfCardIsInDeck(Users user, Cards card){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM deck_cards WHERE deck_id = ? AND card_id = ?");
            sqlStatement.setInt(1, user.getDeck_id());
            sqlStatement.setString(2, card.getCard_id());
            ResultSet resultSet = sqlStatement.executeQuery();
            if(resultSet.next() == false){
                return false;
            } else {
                card.setCard_id(resultSet.getString(1));
                sqlStatement.close();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return false;
    }

    //insert card from stack to users deck
    public Boolean insertCardToDeck(Users user, Cards card){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "INSERT INTO deck_cards (deck_id, card_id) \n" +
                            "VALUES (?,?)  ON CONFLICT DO NOTHING;");
            sqlStatement.setInt(1, user.getDeck_id());
            sqlStatement.setString(2, card.getCard_id());
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            //unitOfWork.finishWork();
        }
        return false;
    }

    //get cards from deck id - needed?
    public void getCardIdsFromDeck(Cards card[], Users user){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM deck_cards WHERE deck_id = ?");
            sqlStatement.setInt(1, user.getDeck_id());
            ResultSet resultSet = sqlStatement.executeQuery();
            int i = 0;
            while(resultSet.next()){
                card[i] = new Cards();
                card[i].setCard_id(resultSet.getString(1));
                if(i == 3){
                    break;
                }
            }
            unitOfWork.commitTransaction();
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }

    //delete the lost card from deck
    public void deleteCardFromDeck(Cards card, int deck_id){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "DELETE FROM deck_cards WHERE card_id = ? AND deck_id = ?");
            sqlStatement.setString(1, card.getCard_id());
            sqlStatement.setInt(2, deck_id);
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }
    //inserts the won card to deck
    public void insertCardToDeck(Cards card, int deck_id){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "INSERT INTO deck_cards (deck_id, card_id) VALUES (?,?)");
            sqlStatement.setInt(1, deck_id);
            sqlStatement.setString(2, card.getCard_id());
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }
}
