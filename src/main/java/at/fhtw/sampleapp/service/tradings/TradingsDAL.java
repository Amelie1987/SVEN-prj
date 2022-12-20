package at.fhtw.sampleapp.service.tradings;

import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;
import at.fhtw.sampleapp.model.Trade;
import at.fhtw.sampleapp.model.Cards;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradingsDAL {
    public TradingsDAL(){}

    public Integer executeDeals(Token token, Trade trade, String cardString) throws SQLException {
        //trade.card_id = card in "store" card.card_id = card offer from request
        UnitOfWork unitOfWork = new UnitOfWork();
        Users offerUser = new Users();
        Users buyUser = new Users();
        Cards offeredCard = new Cards();    // card from trade in DB - needed?
        Cards buyersCard = new Cards();     // Card from buyer in request
        int deck_id;
        if(token.getUsername() == null){          //wrong, invalid token
            System.out.println("Unauthorized User");
            return 401;
        }
        System.out.println("executeDeal activated card Body: " + cardString);
        String tmpString[] = cardString.split("\"", 3);
        System.out.println("Card_Id: " + tmpString[1]);
        buyersCard.setCard_id(tmpString[1]);
        buyUser.setUsername(token.getUsername());
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, buyUser.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {    //user does not exists in DB
                System.out.println("User not found in DB: " + buyUser.getUsername());
                return 401;
            } else {
                buyUser.setId(resultSet.getInt(1));
            }
          //  System.out.println("createDeals - user_id = " + user.getId());
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try {
            // get trade information
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id,user_id,type,mindamage FROM tradingdeals WHERE trade_id = ?");
            prepStatement.setString(1, trade.getTrade_id());
            ResultSet tradeSet = prepStatement.executeQuery();
            while(tradeSet.next()){
                trade.setCard_id(tradeSet.getString(1));
                trade.setUser_id(tradeSet.getInt(2));
                trade.setName(tradeSet.getString(3));
                trade.setMinDamage(tradeSet.getInt(4));
            }
            System.out.println("ExecuteDeal found trade. Min Dmg should be: " + trade.getMinDamage());
            // check if card is in users stack (offerer)
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ? AND card_id = ?");
            prepStatement.setInt(1, trade.getUser_id());
            prepStatement.setString(2, trade.getCard_id());
            ResultSet rSet = prepStatement.executeQuery();
            if(rSet.next() == false) {
                System.out.println("execute Deal - error - offerer does not own this card " + trade.getCard_id());
                return 403;
            }
            // check if card is in users stack (buyer)
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ? AND card_id = ?");
            prepStatement.setInt(1, buyUser.getId());
            prepStatement.setString(2, buyersCard.getCard_id());
            rSet = prepStatement.executeQuery();
            if(rSet.next() == false) {
                System.out.println("execute Deal - error - buyer does not own this card " + trade.getCard_id());
                return 403;
            }
            // check if card is locked in users deck
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT deck_id FROM deck WHERE user_id = ?");
            prepStatement.setInt(1, buyUser.getId());
            ResultSet resultSet = prepStatement.executeQuery();
            if(resultSet.next() == false) {
                // user has no deck
            } else {
                deck_id = resultSet.getInt(1);
                prepStatement = unitOfWork.prepareStatement(
                        "SELECT card_id FROM deck_cards WHERE deck_id = ? AND card_id = ?");
                prepStatement.setInt(1, deck_id);
                prepStatement.setString(2, trade.getCard_id());
                ResultSet Set = prepStatement.executeQuery();
                if(Set.next() == false) {
                    // card is not in users deck
                } else {
                    System.out.println("executeDeal - error: card is locked in users deck");
                    return 403;
                }
            }
            //check if trading deal already exists
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT trade_id FROM tradingdeals WHERE trade_id = ?");
            prepStatement.setString(1, trade.getTrade_id());
            tradeSet = prepStatement.executeQuery();
            if(tradeSet.next() == false) {
                System.out.println("executeDeal - error: deal not found");
                return 404;
            }
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try{
            //check if user is trading with himself
            if(buyUser.getId().equals(trade.getUser_id())){
                System.out.println("Error. User can't trade with himself!");
                return 403;
            }
            //check if card has minDamage
            //delete card from offering users stack
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "DELETE FROM stack WHERE user_id = ? AND card_id = ?");
            preparedStatement.setInt(1,trade.getUser_id());
            preparedStatement.setString(2,trade.getCard_id());
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            preparedStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try{
            //check if user is trading with himself
            if(buyUser.getId().equals(trade.getUser_id())){
                System.out.println("Error. User can't trade with himself!");
                return 403;
            }
            //check if card has minDamage
            //delete card from offering users stack
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "DELETE FROM stack WHERE user_id = ? AND card_id = ?");
            preparedStatement.setInt(1,trade.getUser_id());
            preparedStatement.setString(2,trade.getCard_id());
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            preparedStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try{
            //insert card to buyers stack
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "INSERT INTO stack (user_id, card_id) VALUES (?,?)");
            prepStatement.setInt(1, buyUser.getId());
            prepStatement.setString(2, trade.getCard_id());
            prepStatement.executeUpdate();
            unitOfWork.commitTransaction();
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try{
            //delete card from buyers users stack
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "DELETE FROM stack WHERE user_id = ? AND card_id = ?");
            preparedStatement.setInt(1,buyUser.getId());
            preparedStatement.setString(2, buyersCard.getCard_id());
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            preparedStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try{
            //insert card to offerers stack
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "INSERT INTO stack (user_id, card_id) VALUES (?,?)");
            prepStatement.setInt(1, trade.getUser_id());
            prepStatement.setString(2, buyersCard.getCard_id());
            prepStatement.executeUpdate();
            unitOfWork.commitTransaction();
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        try{
            //delete tradingdeal
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "DELETE FROM tradingdeals WHERE trade_id = ?");
            prepStatement.setString(1, trade.getTrade_id());
            prepStatement.executeUpdate();
            unitOfWork.commitTransaction();
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        unitOfWork.finishWork();
        return 200;
    }

    public String getDeals(Token token) throws SQLException {
        System.out.println("GetDeals active");
        UnitOfWork unitOfWork = new UnitOfWork();
        Users user = new Users();
        String message = "200/";
        Trade trades[] = new Trade[20];           // max of 20 trades ..?
        if(token.getUsername() == null){          //wrong token
            System.out.println("Unauthorized User");
            return "401";
        }
        user.setUsername(token.getUsername());
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT * FROM tradingdeals");
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {
                System.out.println("No trading deals found");
                return "404";
            } else {
                message += "{\n\n";
                int i = 0;
                System.out.println("Trading deals found");
                do {
                    trades[i] = new Trade();
                    trades[i].setTrade_id(resultSet.getString(1));
                    trades[i].setCard_id(resultSet.getString(2));
                    user.setId(resultSet.getInt(3));
                    trades[i].setName(resultSet.getString(4));
                    trades[i].setMinDamage(resultSet.getInt(5));
                    message += " Id: " + trades[i].getTrade_id() + "\n CardToTrade: " + trades[i].getCard_id() + "\n " +
                            "Type: " + trades[i].getName() + "\n MinimumDamage: " + trades[i].getMinDamage() + "\n\n";
                    i++;
                } while(resultSet.next());
            }
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        message += "}\n";
        unitOfWork.finishWork();
        return message;
    }
    public Integer createDeals(Token token, Trade trade) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users user = new Users();
        int deck_id;
        if(token.getUsername() == null){          //wrong, invalid token
            System.out.println("Unauthorized User");
            return 401;
        }
        user.setUsername(token.getUsername());
        try {
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, user.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {    //user does not exists in DB
                System.out.println("User not found in DB: " + user.getUsername());
                return 401;
            } else {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("createDeals - user_id = " + user.getId());
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try {
            // check if card is in users stack
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ? AND card_id = ?");
            prepStatement.setInt(1, user.getId());
            prepStatement.setString(2, trade.getCard_id());
            ResultSet rSet = prepStatement.executeQuery();
            if(rSet.next() == false) {
                System.out.println("create Deal - error - user does not own this card " + trade.getCard_id());
                return 403;
            }
            // check if card is locked in users deck
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT deck_id FROM deck WHERE user_id = ?");
            prepStatement.setInt(1, user.getId());
            ResultSet resultSet = prepStatement.executeQuery();
            if(resultSet.next() == false) {
                // user has no deck
            } else {
                deck_id = resultSet.getInt(1);
                prepStatement = unitOfWork.prepareStatement(
                        "SELECT card_id FROM deck_cards WHERE deck_id = ? AND card_id = ?");
                prepStatement.setInt(1, deck_id);
                prepStatement.setString(2, trade.getCard_id());
                ResultSet Set = prepStatement.executeQuery();
                if(Set.next() == false) {
                    // card is not in users deck
                } else {
                    System.out.println("createDeal - error card is locked in users deck");
                    return 403;
                }
            }
            //check if trading deal already exists
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT trade_id FROM tradingdeals WHERE trade_id = ?");
            prepStatement.setString(1, trade.getTrade_id());
            ResultSet tradeSet = prepStatement.executeQuery();
            if(tradeSet.next() == false) {
                // OK this deal does not exist yet
            } else {
                System.out.println("createDeal - error deal already exists");
                return 409;
            }
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try{
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "INSERT INTO tradingdeals VALUES (?,?,?,?,?);");
            preparedStatement.setString(1,trade.getTrade_id());
            preparedStatement.setString(2,trade.getCard_id());
            preparedStatement.setInt(3,user.getId());
            preparedStatement.setString(4,trade.getName());
            preparedStatement.setInt(5,trade.getMinDamage());
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        unitOfWork.finishWork();
        return 201;
    }
    public Integer deleteDeals(Token token, Trade trade) throws SQLException {
        UnitOfWork unitOfWork = new UnitOfWork();
        Users user = new Users();
        System.out.println("deleteDeals activated");
        if(token.getUsername() == null){          //wrong, invalid token
            System.out.println("Unauthorized User");
            return 401;
        }
        user.setUsername(token.getUsername());
        try {
            // get user_id
            PreparedStatement sqlStatement = unitOfWork.prepareStatement(
                    "SELECT user_id FROM users WHERE username = ?");
            sqlStatement.setString(1, user.getUsername());
            ResultSet resultSet = sqlStatement.executeQuery();
            if (resultSet.next() == false) {    //user does not exists in DB
                System.out.println("User not found in DB: " + user.getUsername());
                return 401;
            } else {
                user.setId(resultSet.getInt(1));
            }
            System.out.println("deleteDeal - user_id = " + user.getId());
            sqlStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try {
            // check if deal_id is valid
            PreparedStatement prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM tradingdeals WHERE trade_id = ?");
            prepStatement.setString(1, trade.getTrade_id());
            ResultSet resultSet = prepStatement.executeQuery();
            if(resultSet.next() == false) {
                return 404;
            } else {
                trade.setCard_id(resultSet.getString(1));
            }
            // check if card is in users stack
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT card_id FROM stack WHERE user_id = ? AND card_id = ?");
            prepStatement.setInt(1, user.getId());
            prepStatement.setString(2, trade.getCard_id());
            ResultSet rSet = prepStatement.executeQuery();
            if(rSet.next() == false) {
                System.out.println("deleteDeal - error - user does not own this card " + trade.getCard_id());
                return 403;
            }
            //check if trading deal belongs to user who wants delete    !! makes no sense !! "A deal with this deal ID already exists"
            prepStatement = unitOfWork.prepareStatement(
                    "SELECT trade_id FROM tradingdeals WHERE trade_id = ? AND user_id = ?");
            prepStatement.setString(1, trade.getTrade_id());
            prepStatement.setInt(2, user.getId());
            ResultSet tradeSet = prepStatement.executeQuery();
            if(tradeSet.next() == false) {
                System.out.println("Error - this deal doesnt belong to the user");
                return 403;
            }   /* else {
                System.out.println("createDeal - error deal already exists");
                return 409;
            } */
            prepStatement.close();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.finishWork();
        }
        try{
            PreparedStatement preparedStatement = unitOfWork.prepareStatement(
                    "DELETE FROM tradingdeals WHERE trade_id = ?");
            preparedStatement.setString(1,trade.getTrade_id());
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
        } catch ( SQLException e){
            System.out.println("SQL SELECT Exception: " + e.getMessage());
            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
        }
        unitOfWork.finishWork();
        return 200;
    }
}

