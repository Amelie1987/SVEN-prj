package at.fhtw.sampleapp.dal.repository;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleRepository {
    private UnitOfWork unitOfWork;

    public BattleRepository(UnitOfWork unitOfWork) { this.unitOfWork = unitOfWork; }

    //write battlelog into db
    public void writeBattleLog( int battle_id, String battleLog){
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                        "INSERT INTO battlelog (battle_id,battlelog) VALUES (?,?)");
                sqlStatement.setInt(1, battle_id);
                sqlStatement.setString(2, battleLog);
                sqlStatement.executeUpdate();
                unitOfWork.commitTransaction();
                sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }

    //verify token and get user_id
    public Integer isRegisteredForBattle(Users user){
        try {
            // get user_id
            int battle_id = 0;
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT battle_id FROM battle WHERE player1 = ?");
            sqlStatement.setInt(1, user.getId());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();

            if(resultSet.next() == false){
                sqlStatement.close();
                return 0;
            } else {
                battle_id = resultSet.getInt(1);
                sqlStatement.close();
                return battle_id;
            }
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return 0;
    }

    //register player1 or 2 for battle
    public void registerForBattle(Users user, int battle_id, int player){
        try {
            PreparedStatement sqlStatement;
            if( player == 1) {
                sqlStatement = unitOfWork.prepareStatement(
                        "INSERT INTO battle (player1) VALUES (?)");
                sqlStatement.setInt(1, user.getId());
                sqlStatement.executeUpdate();
                unitOfWork.commitTransaction();
                sqlStatement.close();
            } else if (player == 2) {
                sqlStatement = unitOfWork.prepareStatement(
                        "UPDATE battle SET player2 = ? WHERE battle_id = ? ");
                sqlStatement.setInt(1, user.getId());
                sqlStatement.setInt(2, battle_id);
                sqlStatement.executeUpdate();
                unitOfWork.commitTransaction();
                sqlStatement.close();
            }
            System.out.println("Player " + player + "registered for battle.");
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
    }

    //returns battle id
    public Integer getBattleId(){
        int battle_id = 0;
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT battle_id FROM battle");
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            while(resultSet.next()){
                battle_id = resultSet.getInt(1);
            }
            sqlStatement.close();

        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        return battle_id;
    }
    //set IDs from player one and two
    public void setPlayerIds(int battle_id, Users playerOne, Users playerTwo){
        try {

            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT player1, player2 FROM battle WHERE battle_id = ?");
            sqlStatement.setInt(1, battle_id);
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();
            while(resultSet.next()){
               playerOne.setId(resultSet.getInt(1));
               playerTwo.setId(resultSet.getInt(2));
            }
           System.out.println("Player 1 id: " + playerOne.getId() + " Player 2 id: " + playerTwo.getId());
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
    }
    //set deck_id from player one and two
    public void setDeckIds(Users user){
        try {

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
    }
}
