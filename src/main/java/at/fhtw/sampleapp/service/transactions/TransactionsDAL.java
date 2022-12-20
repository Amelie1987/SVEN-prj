package at.fhtw.sampleapp.service.transactions;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionsDAL {
    public Integer acquirePackage(Token token) throws SQLException {  //acquire package to another user
        UnitOfWork unitOfWork = new UnitOfWork();
        Users dbUser = new Users();
        Cards cards[] = new Cards[5];
        int package_id = 0;
        if(token.getToken_id() == null) {
            return 401;             // authentication information is missing or invalid
        }
        System.out.println("ACQUIRE PACKAGES ACTIVATED");
        try {
            // get user_id and check payment
            PreparedStatement userIdStatement = unitOfWork.prepareStatement(
                    "SELECT user_id, coins FROM users WHERE username = ? ");
            userIdStatement.setString(1, token.getUsername());
            ResultSet userSet = userIdStatement.executeQuery();


            if (userSet != null) {
                while (userSet.next()) {
                    dbUser.setId(userSet.getInt(1));
                    dbUser.setCoins(userSet.getInt(2));
                }
            }
            System.out.println("User_id: " + dbUser.getId() + " has coins: " + dbUser.getCoins());
            if (dbUser.getCoins() < 5) {
                System.out.println("Error - User has no coins left");
                unitOfWork.finishWork();
                return 403;
            }
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try {
            //SELECT package
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT package_id FROM packages " +
                            "ORDER BY package_id ASC LIMIT 1");
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();

            while (resultSet.next()) {
                package_id = resultSet.getInt(1);
            }
            System.out.println("package_id : " + package_id);
            if(package_id == 0){
                System.out.println("Error - No packages avaible");
                return 404;
            }
            sqlStatement.close();
            //get card ids from package
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM packages_cards WHERE package_id = ?");
            preparedStatement.setInt(1, package_id);
            ResultSet cardSet = preparedStatement.executeQuery();
            unitOfWork.commitTransaction();
            int i = 0;
            while (cardSet.next()) {
                cards[i] = new Cards();
                cards[i].setCard_id(cardSet.getString( 1));
                System.out.println("card_id " + cards[i].getCard_id());
                i++;
            }
            preparedStatement.close();

        } catch (SQLException e) {
                System.out.println("SQL SELECT Exception: " + e.getMessage());
                unitOfWork.rollbackTransaction();
                unitOfWork.finishWork();
        }
        try {
            // insert cards in the users stack
            PreparedStatement thisStatement = unitOfWork.prepareStatement("");
            for(int j = 0; j < 5; j++) {
                thisStatement = unitOfWork.prepareStatement(
                        "INSERT INTO stack (user_id, card_id) VALUES (?,?)  ON CONFLICT DO NOTHING");
                thisStatement.setInt(1, dbUser.getId());
                thisStatement.setString(2, cards[j].getCard_id());
                thisStatement.executeUpdate();
            }
            thisStatement.close();
            unitOfWork.commitTransaction();
            /*
            PreparedStatement myStatement = unitOfWork.prepareStatement(
                    "INSERT INTO packages_cards (package_id,card_id) VALUES (?,?)");
            myStatement.setInt(1, package_id);
            myStatement.setString(2, cards[i].getCard_id());
            myStatement.executeUpdate();
            unitOfWork.commitTransaction();
            myStatement.close();
            */
            // delete package from table packages and package_cards
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "DELETE FROM packages WHERE package_id = ?;" +
                            "DELETE FROM packages_cards WHERE package_id = ?;");
            prepStatement.setInt(1,package_id);
            prepStatement.setInt(2,package_id);
            prepStatement.executeUpdate();
            unitOfWork.commitTransaction();
            prepStatement.close();

            // user pays 5 coins
            int currentCoins = dbUser.getCoins() - 5;
            System.out.println("Username " + dbUser.getUsername() + " coins: " + dbUser.getCoins() + "coins after transfair: "+ currentCoins);
            PreparedStatement payStatement = unitOfWork.prepareStatement(
                    "UPDATE users SET coins = ? WHERE user_id = ?;");
            payStatement.setInt(1, currentCoins);
            payStatement.setInt(2, dbUser.getId());
            payStatement.executeUpdate();
            unitOfWork.commitTransaction();
            payStatement.close();
            unitOfWork.finishWork();
        } catch (SQLException e) {
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        return 200;
    }
}
