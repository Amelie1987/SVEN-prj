package at.fhtw.sampleapp.dal.repository;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;
import at.fhtw.sampleapp.service.battle.BattleLogic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private UnitOfWork unitOfWork;

    public UserRepository(UnitOfWork unitOfWork) { this.unitOfWork = unitOfWork; }

    //set Elo + 5 for winner
    public void setEloForWinner(Users user){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "UPDATE users SET elo = (elo + 5), wins = (wins + 1)  WHERE user_id = ?");
            sqlStatement.setInt(1, user.getId());
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();
            
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }

    //set Elo - 3 for loser
    public void setEloForLoser(Users user){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "UPDATE users SET elo = (elo - 3), losses = (losses + 1) WHERE user_id = ?");
            sqlStatement.setInt(1, user.getId());
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();

            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }
    //get user_id by token
    public Users getUserIdByToken(Token token){
        Users user = new Users();
        //user.setUsername(token.getUsername());
        try {
            // get user_id
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM tokens WHERE token_id = ?");
            sqlStatement.setString(1, token.getToken_id());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            while(resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            System.out.println("UserRepository getIdByToken - user_id = " + user.getId());
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return user;
    }

    //get UserID by token - not needed
    public Users getUserId(Token token){
        Users user = new Users();
        user.setUsername(token.getUsername());
        try {
            // get user_id
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, user.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            while(resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            System.out.println("UserRepository is working ... user_id = " + user.getId());
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return user;
    }
    // get Deck id from user id
    public Users getDeckId(Users user){
        try {
            int deck_id = 0;
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT deck_id FROM deck WHERE user_id = ?");
            sqlStatement.setInt(1, user.getId());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            while(resultSet.next()){
                user.setDeck_id(resultSet.getInt(1));
            }
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return user;
    }


}
