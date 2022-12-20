package at.fhtw.sampleapp.service.session;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDAL {
    public SessionDAL() {
    }

    public boolean login(Users users) throws SQLException {  //if users exists in DB - login by INSERT a token
      UnitOfWork unitOfWork = new UnitOfWork();
      //  DbConnect dbConnect = new DbConnect();
      //  Connection connect = dbConnect.connect();
      //  connect.setAutoCommit(false);
        try {

            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id,username,password FROM users " +
                            "WHERE username = ? AND password = ? ");
            sqlStatement.setString(1, users.getUsername());
            sqlStatement.setString(2, users.getPassword());
            ResultSet resultSet = sqlStatement.executeQuery();
            Users dbUser = new Users();
            if(resultSet != null) {
                while(resultSet.next()) {
                    dbUser.setId(resultSet.getInt(1));
                    dbUser.setUsername(resultSet.getString(2));
                    dbUser.setPassword(resultSet.getString(3));
                }
               // System.out.println("Curl entry " + users.getUsername() + " pw: " + users.getPassword());
                System.out.println("DB entry " + dbUser.getUsername() + " user_id: " + dbUser.getId());
            } else {
                System.out.println("User not found.");
            }
            if(users.getUsername().equals(dbUser.getUsername()) &&
                    users.getPassword().equals(dbUser.getPassword())){
                System.out.println("Login Data Correct.");
                try {
                    PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                            "INSERT INTO tokens (token_id, user_id) VALUES (?,?)");
                    String tokenString = "Basic " + dbUser.getUsername() + "-mtcgToken";
                    System.out.println("Token name : " + tokenString);
                    preparedStatement.setString(1, tokenString);
                    preparedStatement.setInt(2, dbUser.getId());
                    preparedStatement.executeUpdate();
                    unitOfWork.commitTransaction();
                    preparedStatement.close();
                    unitOfWork.finishWork();

                } catch (SQLException e) {
                    System.out.println("SQL SELECT Exception: " + e.getMessage());
                    unitOfWork.rollbackTransaction();
                    unitOfWork.finishWork();
                }
                return true;
            }
            sqlStatement.close();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        return false;
    }
}
