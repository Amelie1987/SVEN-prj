package at.fhtw.sampleapp.service.battle;

import at.fhtw.sampleapp.model.Cards;
import java.awt.*;

public class BattleLogic {

    private CardWon cardWon;

    public BattleLogic() { }

    public enum CardWon {
        EMPTY,
        PLAYERONE,
        PLAYERTWO,
        DRAW
    }

    // get multiplier from element effectiveness
    public double getMultiplier(Cards cardOne, Cards cardTwo){
        //Goblins are too afraid to attack Dragons
        if(cardOne.getType().equals("Goblin") && cardTwo.getType().equals("Dragon")){
            return 0;
        }
        //orks can't damage wizzards
        if(cardOne.getType().equals("Orks") && cardTwo.getType().equals("Wizzard")){
            return 0;
        }
        //waterspell kills Knight
        if(cardOne.getName().equals("WaterSpell") && cardTwo.getType().equals("Knight")){
            return 50;
        }
        //Kraken is immune to spells
        if(cardOne.getType().equals("Spell") && cardTwo.getType().equals("Kraken")){
            return 0;
        }
        //dragons are too polite to harm fireelves
        if(cardOne.getType().equals("Dragon") && cardTwo.getName().equals("FireElf")){
            return 0;
        }
        //same element vs same element and monsters return multiplier 1
        if(cardOne.getElement().equals(cardTwo.getElement()) || !cardOne.getType().equals("Spell")){
            return 1;
        } else if (cardOne.getElement().equals("Fire") && cardTwo.getElement().equals("Normal")){
            return 2;
        } else if(cardOne.getElement().equals("Fire") && cardTwo.getElement().equals("Water")){
            return 0.5;
        } else if (cardOne.getElement().equals("Water") && cardTwo.getElement().equals("Normal")){
            return 0.5;
        } else if (cardOne.getElement().equals("Water") && cardTwo.getElement().equals("Fire")){
            return 2;
        } else if (cardOne.getElement().equals("Normal") && cardTwo.getElement().equals("Water")){
            return 2;
        } else if (cardOne.getElement().equals("Normal") && cardTwo.getElement().equals("Fire")){
            return 0.5;
        }
        return 1;       // elements are the same
    }
    // get loser card and sets cardWon (PLAYERONE, PLAYERTWO or DRAW)
    public Cards getLoserCard(Cards cardOne, Cards cardTwo){
        if(!cardOne.getType().equals("Spell") && !cardTwo.getType().equals("Spell")) {
            //  no type effects because of pure monster fights
            if(cardOne.getDamage() > cardTwo.getDamage()){
                this.cardWon = CardWon.PLAYERONE;
                return cardTwo;
            } else if(cardOne.getDamage() < cardTwo.getDamage()){
                this.cardWon = CardWon.PLAYERTWO;
                return cardOne;
            } else if(cardOne.getDamage() == cardTwo.getDamage()) {
                this.cardWon = CardWon.DRAW;
                return null;
            }
        } else {
            if(cardOne.getDamage()*getMultiplier(cardOne, cardTwo) > cardTwo.getDamage()*getMultiplier(cardTwo, cardOne)){
                this.cardWon = CardWon.PLAYERONE;
                return cardTwo;
            } else if(cardOne.getDamage()*getMultiplier(cardOne, cardTwo) < cardTwo.getDamage()*getMultiplier(cardTwo, cardOne)){
                this.cardWon = CardWon.PLAYERTWO;
                return cardOne;
            } else if(cardOne.getDamage()*getMultiplier(cardOne, cardTwo) == cardTwo.getDamage()*getMultiplier(cardTwo, cardOne)) {
                this.cardWon = CardWon.DRAW;
                return null;
            }
        }
        this.cardWon = CardWon.EMPTY;
        return null;
    }

    //prints a goblin if user gets card from stack because the WaterSpell is amazing
    public String printGoblin(){
        String goblin = "                                                                                                    \n" +
                "                                                   .   *                                            \n" +
                "                                                (,      #                                           \n" +
                "                                      %(.    #(          %                                          \n" +
                "                                      &,@,,*             &&                                         \n" +
                "                                      ..%%@%(.# /@   .% %(//,@                                      \n" +
                "                                       &(@@((,  **.@@.(*%&, @                                       \n" +
                "                                       (@&% / /%@   # */(%%&#                                       \n" +
                "                                      / /&#(, ( // #@%&  /@                                         \n" +
                "                                    ...%#@(@/ ##(,&& /%&,% @                                        \n" +
                "                                  @%%&&%*@@(/.& ..*@&(&%/// %@                                      \n" +
                "                             %*/* (  .,,&*(*&&%%/,.(.&/      #&,*@&.                                \n" +
                "                         &*,(, (,/..#(/ / ,@@,@(#/#,&.( /,/ # %*%%*.%&#,  *                         \n" +
                "                 *&/ ( ./.,,**/,**(#(%(#**(. (   , ,*(#//,/*/.,/.#/&/,*&   #                        \n" +
                "               % *//**#&%&/%(% ..,../(&%*%*,  #//.(&%#((,(%((.%/  .***#*/%  %### /*                 \n" +
                "           .%*  ,#((**   ,  , *( . (//& , ,% .# *#(% #%/@// /(  *&%&#/**. *(/   && #    ,(%*        \n" +
                "         .%(.(*%,/.     *  .,* .*  (/./.,,/.. /%/*#.%(%#(*#   ((      *&(,(  %%/     .  .#&./,  ,%. \n" +
                "       @% ,**%/      %%    . *(    ,,(*./ #  .#. #  ../(*/..    %        *(/(*, #*               &  \n" +
                "     %* ..%,     ##      %(      .(*/  /,..%/#.  ,% /#(,.,  *     &        /./%/,% ,#            @  \n" +
                "    &  #, @.           (        , ,#(,/,..(* &. */(.@@%/,/.  %.     @        &..(/%./%&.       *&   \n" +
                "    #/#(@*,#( ,(@&.              *,*/(*.  .#/.,**(/#,*/.  %    *      #.         ## , *,.,   ,@     \n" +
                "   /(@ ,  @ .,                    *. /,..*,*/* / *#,../(   %     /       @@   .%*@& .,%(,.          \n" +
                "    /.   /    % (                 , *#( (.& * # (#*(,(#      .     (        &,   @(.(&%@&           \n" +
                "    *( &     .  . /               . /(.,** ,./.((/*/* //,     #     ./        .&.% . ,.             \n" +
                "    &.  /     #      ..           #,.  /**** %(&*/*%.#(*(            (            / %               \n" +
                "   (@(*@,,               (        .%//. ,*./&/(,%#&,%. /                 &            %#%           \n" +
                "           #     /            *   ( /.,.* ,.&(,%%* (    .                .%               &         \n" +
                "               &@&                  ,/#  .%/*.%/        ,                  /(           .#          \n" +
                "                   &        ,        %. *,/.,,(*         *                             @            \n" +
                "                     *.        .. ,.(  ,#*.,,,,(          %                           *             \n" +
                "                       *       (*,, /( /%@%,(.*/           @                         @.             \n" +
                "                         ,/   %#*& @ .#  (.,,(. **/ / .     &                 #@@@&.                \n" +
                "                             %    %        /@,****,(    .(   %               @                      \n" +
                "                                /@           ##(&,**&     ,*  ,            ,#                       \n" +
                "                                     &(      ( *// /%         * &.      &,                          \n" +
                "                                        *. %   ,%,, ,%           (&   @                             \n" +
                "                                           &&    #(( ,             @ * /                            \n" +
                "                                             /@&%.*,.%,/&            %,&,&&,                        \n" +
                "                                                   %*( ##.               @                          \n" +
                "                                                  &  **#                                            \n" +
                "                                                 &    @                                             \n" +
                "                                              &%.%,  &                                              \n" +
                "                                             #@@/#/&(.   \n";
        return goblin;
    }
    public CardWon getCardWon() {
        return cardWon;
    }
}
