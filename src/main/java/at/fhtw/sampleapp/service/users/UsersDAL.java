package at.fhtw.sampleapp.service.users;

import at.fhtw.db.DbConnect;
import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Users;
import at.fhtw.sampleapp.model.Token;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAL {
    public UsersDAL() {

    }
    // looks if user exist, token is valid and sends user stats to client
    public String getUsersScore(Token token) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users users[] = new Users[10];
        String message = "200/ {\n";

        if(token.getUsername() == null){          //no token
            System.out.println("Unauthorized User");
            return "401";
        }
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT name,elo,wins,losses FROM users ORDER BY elo DESC;");
            ResultSet resultSet = sqlStatement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                users[i] = new Users();
                users[i].setName(resultSet.getString( 1));
                users[i].setElo(resultSet.getInt( 2));
                users[i].setWins(resultSet.getInt( 3));
                users[i].setLosses(resultSet.getInt( 4));
                message += "Name: " + users[i].getName() + "\n Elo:  " + users[i].getElo() +
                            "\nWins: " + users[i].getWins() + "\nLosses: " + users[i].getLosses() + "\n\n";
                i++;
            }
            message += "}";
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        unitOfWork.finishWork();
        return message;
    }
    // looks if user exist, token is valid and update users Data
    public Integer updateUsers(String username, Token token, Users user) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        System.out.println("updateUsers - Get Userdata from: " + username + "Compare token: " + token.getUsername());
        System.out.println("Read Values: Name: " + user.getName() + " Bio: " + user.getBio() + "Image: " + user.getImage());
        if(!token.getUsername().equals(username)){          //wrong token
            System.out.println("Unauthorized User: " + username);
            return 401;
        }
        user.setUsername(username);
        try {
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            prepStatement.setString(1, user.getUsername());
            ResultSet resultSet = prepStatement.executeQuery();
            // unitOfWork.commitTransaction();

            if (resultSet.next() == false) {
                System.out.println("User not found in DB: " + user.getUsername());
                return 404;
            } else {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("getUser - user_id = " + user.getId());
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
        }
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "UPDATE users SET name=?, bio=?, image=? WHERE user_id =?");
            sqlStatement.setString(1, user.getName());
            sqlStatement.setString(2, user.getBio());
            sqlStatement.setString(3, user.getImage());
            sqlStatement.setInt(4, user.getId());
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();
            sqlStatement.close();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            System.out.println("SQL Update Row Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        unitOfWork.finishWork();
        return 200;
    }
    // looks if user exist, token is valid and sends user stats to client
    public String getUsersStats(Token token) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users user = new Users();
        String message = "200/";

        if(token.getUsername() == null){          //wrong token
            System.out.println("Unauthorized User");
            return "401";
        }
        user.setUsername(token.getUsername());
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, user.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {
                System.out.println("User not found in DB: " + user.getUsername());
                return "404";
            } else {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("getUsersStats - user_id = " + user.getId());
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try {
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT name,elo,wins,losses FROM users WHERE user_id = ?");
            prepStatement.setInt(1, user.getId());
            ResultSet userSet = prepStatement.executeQuery();
            while(userSet.next()) {
                user.setName(userSet.getString(1));
                user.setElo(userSet.getInt(2));
                user.setWins(userSet.getInt(3));
                user.setLosses(userSet.getInt(4));
            }
            System.out.println("getUser - user_id = " + user.getId());
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        message += "{ \n Name: " + user.getName() + "\n Elo: " + user.getElo() + "\n Wins: " + user.getWins() +
                "\n Losses: " + user.getLosses() + "\n }";
        unitOfWork.finishWork();
        return message;
    }

    // looks if user exist, token is valid and sends user data to client
    public String getUsers(String username, Token token) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users user = new Users();
        String message = "200/";
        System.out.println("getUsers - Get Userdata from: " + username + "Compare token: " + token.getUsername());
        if(!token.getUsername().equals(username)){          //wrong token
            System.out.println("Unauthorized User: " + username);
            return "401";
        }
        user.setUsername(username);
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, user.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {
                System.out.println("User not found in DB: " + user.getUsername());
                return "404";
            } else {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("getUser - user_id = " + user.getId());
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try {
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT name,bio,image,elo FROM users WHERE user_id = ?");
            prepStatement.setInt(1, user.getId());
            ResultSet userSet = prepStatement.executeQuery();
            while(userSet.next()) {
                user.setName(userSet.getString(1));
                user.setBio(userSet.getString(2));
                user.setImage(userSet.getString(3));
                user.setElo(userSet.getInt(4));
            }
            System.out.println("getUser - user_id = " + user.getId());
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        message += "{ \n Name: " + user.getName() + "\n Bio: " + user.getBio() + "\n Image: " + user.getImage() +
                "\n Elo: " + user.getElo() + "\n }";
        unitOfWork.finishWork();
        return message;
    }

  //  @Override
    public boolean addUsers(Users users) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users dbUser = new Users();
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT username FROM users WHERE username = ? ");
            sqlStatement.setString(1, users.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            if(resultSet != null) {
                while(resultSet.next()) {
                    dbUser.setUsername(resultSet.getString(1));
                }
            }
            if(users.getUsername().equals(dbUser.getUsername())){
                System.out.println("User already exists in DB.");
                sqlStatement.close();
                unitOfWork.finishWork();
                return false;
            }
            sqlStatement.close();
        } catch (SQLException e) {
        System.out.println("SQL SELECT Exception: " + e.getMessage());
        unitOfWork.finishWork();
        }
        try {
           PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                   "INSERT INTO users (username,password, elo, coins) VALUES (?,?,?,?)");
            sqlStatement.setString(1, users.getUsername());
            sqlStatement.setString(2, users.getPassword());
            sqlStatement.setInt(3, 100);
            sqlStatement.setInt(4, 20);
            sqlStatement.executeUpdate();
            unitOfWork.commitTransaction();
            sqlStatement.close();
        } catch (SQLException e) {
            unitOfWork.rollbackTransaction();
            System.out.println("SQL Update Row Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        unitOfWork.finishWork();
        return true;
    }
}