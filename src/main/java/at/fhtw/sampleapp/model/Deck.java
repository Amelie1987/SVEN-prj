package at.fhtw.sampleapp.model;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Deck {

    public String name;

    public int id;
    public Cards card[];

    public List cards = new List();
    public Deck(){}

 /*   public Deck(List cards){
        this.cards = cards;
    }  */
    public Deck(Cards card[]){

        for(int  i = 0; i < Array.getLength(card); i++){
            this.card[i] = card[i];
        }
    }
    public Boolean isValid(){
        // count Cards
        if( Array.getLength(card) < 4 || Array.getLength(card) > 4){
            return false;
        }
        return true;
    }

    public Boolean isEmpty(){
        // count Cards
        if( Array.getLength(card) == 0){
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cards[] getCard() {
        return card;
    }

    public void setCard(Cards[] card) {
        this.card = card;
    }
}
