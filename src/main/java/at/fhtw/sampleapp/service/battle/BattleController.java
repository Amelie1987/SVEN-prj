package at.fhtw.sampleapp.service.battle;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.dal.UnitOfWork;
import at.fhtw.sampleapp.dal.repository.BattleRepository;
import at.fhtw.sampleapp.dal.repository.CardRepository;
import at.fhtw.sampleapp.dal.repository.UserRepository;
import at.fhtw.sampleapp.model.Cards;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Users;

import java.util.concurrent.TimeUnit;
import at.fhtw.sampleapp.service.deck.DeckDAL;

import java.sql.SQLException;

public class BattleController {

    private BattleDAL battleDAL;
    private BattleLogic battleLogic;


    private int numberOfPlayers = 0;

    private Users playerOne;

    private Users playerTwo;
    public BattleController(BattleDAL battleDAL, BattleLogic battleLogic) {
        this.battleLogic = battleLogic;
        this.battleDAL = battleDAL;
    }

    // POST /battles
    public Response  startBattle(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        UnitOfWork unitOfWorkBattle = new UnitOfWork();
        UnitOfWork unitOfWorkUsers = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWorkUsers);
        BattleRepository battleRepository = new BattleRepository(unitOfWorkBattle);
        int battle_id = 0;
        try (unitOfWork){
            Token token = new Token();
            String message = "";
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id() + " - starts battle");
            Users user = new UserRepository(unitOfWork).getUserIdByToken(token);
            if(numberOfPlayers == 0){
                numberOfPlayers++;
                battleRepository.registerForBattle(user, 0, 1); // register player 1
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: " + token.getUsername() + "  registered for battle }"
                );
            } else {
                TimeUnit.SECONDS.sleep(2);
                battle_id = battleRepository.getBattleId();
                battleRepository.registerForBattle(user, battle_id, 2); // register player 2
                numberOfPlayers = 0;
            }
            //set Player One and Two
            playerOne = new Users();
            playerTwo = new Users();
            battleRepository.setPlayerIds(battle_id, playerOne, playerTwo);
            battleRepository.setDeckIds(playerOne);
            battleRepository.setDeckIds(playerTwo);
            System.out.println("Deck Id player 1: " +playerOne.getDeck_id() + " Deck player 2: " + playerTwo.getDeck_id());
            //set card information for both decks
            CardRepository cardRepository = new CardRepository(unitOfWork);
            Cards cardOne = new Cards();
            Cards cardTwo = new Cards();
            String battleLog = "";
            int rounds = 0;
            int numCardsPlayerOne = 4;
            int numCardsPlayerTwo = 4;
            while(true){
                cardRepository.getRandomCard( playerOne, cardOne);
                cardRepository.getRandomCard(playerTwo, cardTwo);
                if(token.getToken_id() == null || user.getId() == null){
                    message+= "401/";
                    break;
                }
                if(playerOne.getLost() || playerTwo.getLost() || numCardsPlayerOne == 0 || numCardsPlayerTwo == 0){
                    System.out.println(("Game finished - " + battleLogic.getCardWon() + " wons."));
                    battleLog += "Game finished - " + battleLogic.getCardWon() + " wons.\n";
                    if(battleLogic.getCardWon().equals(BattleLogic.CardWon.PLAYERONE)){
                        userRepository.setEloForWinner(playerOne);
                        userRepository.setEloForLoser(playerTwo);
                    } else if(battleLogic.getCardWon().equals(BattleLogic.CardWon.PLAYERTWO)){
                        userRepository.setEloForWinner(playerTwo);
                        userRepository.setEloForLoser(playerOne);
                    }
                    message += "200/" + battleLog;
                    break;
                }
                Cards cardToSwitch;
                cardRepository.setCardStats(cardOne);
                cardRepository.setCardStats(cardTwo);
                System.out.println("PlayerOne Card: " + cardOne.getName() + " Dmg: " + cardOne.getDamage() +
                        " PlayerTwo Card: " + cardTwo.getName() + " Dmg: " + cardTwo.getDamage());
                battleLog += "PlayerOne Card: " + cardOne.getName() + " Dmg: " + cardOne.getDamage() +
                        " PlayerTwo Card: " + cardTwo.getName() + " Dmg: " + cardTwo.getDamage() + "\n ";
                cardToSwitch = battleLogic.getLoserCard(cardOne, cardTwo); //sets enum cardWon too
                // SPECIAL FEATURE if a waterSpell meets a Goblin the user who played the WaterSpell gets a random card
                // from his stack into the deck ("einen Schaumgeborenen") && WaterSpell must beat Goblin by stats without multipliers
                if((cardOne.getName().equals("WaterSpell") && cardTwo.getType().equals("Goblin"))
                        && (cardOne.getDamage() > cardTwo.getDamage())){
                    Cards newCard = new Cards();
                    if(cardRepository.getRandomCardFromStack(playerOne, newCard) && !cardRepository.proofIfCardIsInDeck(playerOne, newCard)){
                        cardRepository.insertCardToDeck(playerOne, newCard);
                        cardRepository.setCardStats(newCard);
                        battleLog += "Player One got a new card! Congratulation it's: " + newCard.getName() +
                                " with dmg: " + newCard.getDamage() + "\n";
                        System.out.println("Player One got a new card! Congratulation it's:" + newCard.getName() +
                                        " with dmg:" + newCard.getDamage());
                        numCardsPlayerOne++;
                    } else {
                        battleLog += "no cards left in users stack or random choosen card is already in deck. Sry player One.\n";
                    }
                }
                if((cardTwo.getName().equals("WaterSpell") && cardOne.getType().equals("Goblin"))
                        && (cardTwo.getDamage() > cardOne.getDamage())){
                    Cards newCard = new Cards();
                    if(cardRepository.getRandomCardFromStack(playerTwo, newCard) && !cardRepository.proofIfCardIsInDeck(playerTwo, newCard)){
                        cardRepository.insertCardToDeck(playerTwo, newCard);
                        cardRepository.setCardStats(newCard);
                        battleLog += "Player Two got a new card! Congratulation it's: " + newCard.getName() +
                                " with dmg: " + newCard.getDamage() + "\n";
                        System.out.println("Player Two got a new card! Congratulation it's:" + newCard.getName() +
                                " with dmg:" + newCard.getDamage());
                        numCardsPlayerTwo++;

                    } else {
                        battleLog += "no cards left in users stack or random choosen card is already in deck. Sry player Two.\n";
                    }
                }
                if(battleLogic.getCardWon().equals(BattleLogic.CardWon.PLAYERONE)){
                    cardRepository.deleteCardFromDeck(cardToSwitch, playerTwo.getDeck_id());
                    if(!cardRepository.proofIfCardIsInDeck(playerOne, cardToSwitch)) {
                        cardRepository.insertCardToDeck(cardToSwitch, playerOne.getDeck_id());
                    }
                    numCardsPlayerOne++;
                    numCardsPlayerTwo--;
                    battleLog += "Lost Card: " + cardToSwitch.getName() + "\nPlayerOneCards: " + numCardsPlayerOne +
                            " PlayerTwoCards: " + numCardsPlayerTwo + "\n";
                    System.out.println("Lost Card: " + cardToSwitch.getName() + "\nPlayerOneCards: " + numCardsPlayerOne +
                            " PlayerTwoCards: " + numCardsPlayerTwo);
                } else if (battleLogic.getCardWon().equals(BattleLogic.CardWon.PLAYERTWO)) {
                    cardRepository.deleteCardFromDeck(cardToSwitch, playerOne.getDeck_id());
                    if(!cardRepository.proofIfCardIsInDeck(playerTwo, cardToSwitch)) {
                        cardRepository.insertCardToDeck(cardToSwitch, playerTwo.getDeck_id());
                    }
                    numCardsPlayerOne--;
                    numCardsPlayerTwo++;
                    battleLog += "Lost Card: " + cardToSwitch.getName() + "\nPlayerOneCards: " + numCardsPlayerOne +
                            " PlayerTwoCards: " + numCardsPlayerTwo + "\n";
                    System.out.println("Lost Card: " + cardToSwitch.getName() + " \nPlayerOneCards: " + numCardsPlayerOne +
                            " PlayerTwoCards: " + numCardsPlayerTwo);
                } else if (battleLogic.getCardWon().equals(BattleLogic.CardWon.DRAW)){
                    battleLog += "It's a draw.\n";
                    System.out.println("It's a draw.");
                }


                //System.out.println("Won: " + cardToSwitch.getName());
                System.out.println(" Result - Won: " + battleLogic.getCardWon());
                rounds++;
                if(rounds == 100){
                    System.out.println(battleLogic.printGoblin());
                    String goblinString = battleLogic.printGoblin();        //to get clean battleLog for DB entry
                    System.out.println("Battle finished after 100 Rounds. It's a draw.");
                    message += "200/" + battleLog + goblinString + ("Battle finished after 100 Rounds. It's a draw.\n");
                    battleLog += "Battle finished after 100 Rounds. It's a draw.\n";
                    break;
                }
            }
            battleRepository.writeBattleLog(battle_id,battleLog);
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);
            unitOfWork.finishWork();
            unitOfWorkBattle.finishWork();
            unitOfWorkUsers.finishWork();
            if(responseCode[0].equals("200")) {         // OK and show card_ids
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: " + responseCode[1] + " \n}\n\n"
                );
            }  else if(responseCode[0].equals("401")){
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"Unauthorized\" }"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            unitOfWork.finishWork();
            unitOfWorkBattle.finishWork();
            //unitOfWorkCard.finishWork();
            unitOfWorkUsers.finishWork();
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}
