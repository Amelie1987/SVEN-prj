package at.fhtw.sampleapp.service.packages;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Users;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Token;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackagesDAL {
    public PackagesDAL() {
    }

    public Integer createPackage(Cards[] cards, Token token) throws SQLException {  //creates packages when admin is logged in
        UnitOfWork unitOfWork = new UnitOfWork();
        if(token.isAdmin()){
            System.out.println("Token accepted - username = " + token.getUsername());
        } else if(token.getToken_id() == null) {
            return 401;             // authentication information is missing or invalid
        } else {
            return 403;             // Provided user is not admin
        }
        try {
             //get user_id
            Users user = new Users();
            int package_id = 0;
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, token.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            unitOfWork.commitTransaction();

            while(resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            System.out.println("user_id = " + user.getId());
            sqlStatement.close();
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "INSERT INTO packages (user_id) VALUES (?)");
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            preparedStatement.close();
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT package_id FROM packages WHERE user_id = ? ORDER BY package_id DESC LIMIT 1");
            prepStatement.setInt(1,user.getId());
            ResultSet rSet = prepStatement.executeQuery();
            unitOfWork.commitTransaction();

            while(rSet.next()){
                package_id = rSet.getInt(1);
            }
            prepStatement.close();
            System.out.println("package_id = " + package_id);
            // check if card already exists in package
            for(int i = 0; i < 5; i++){

                PreparedStatement thisStatement = unitOfWork.prepareStatement(
                        "SELECT card_id FROM packages_cards WHERE package_id = ? AND card_id = ?");
                thisStatement.setInt(1, package_id);
                thisStatement.setString(2, cards[i].getCard_id());
                ResultSet doubleCard = thisStatement.executeQuery();
                unitOfWork.commitTransaction();

                if(doubleCard.next() == false) {             // return error when card already exists in package
                   // System.out.println("empty result set - card is not double");
                } else {
                    System.out.println("Double card in package - can't create. " + doubleCard.getString(1));
                    unitOfWork.finishWork();
                    return 409;
                }
                thisStatement.close();
                PreparedStatement myStatement = unitOfWork.prepareStatement(
                        "INSERT INTO packages_cards (package_id,card_id) VALUES (?,?)");
                myStatement.setInt(1, package_id);
                myStatement.setString(2, cards[i].getCard_id());
                myStatement.executeUpdate();
                unitOfWork.commitTransaction();
                myStatement.close();
            }
            unitOfWork.finishWork();

        } catch (SQLException e) {
        System.out.println("SQL SELECT Exception: " + e.getMessage());
        unitOfWork.rollbackTransaction();
        unitOfWork.finishWork();
        }
        return 201;
    }
}
