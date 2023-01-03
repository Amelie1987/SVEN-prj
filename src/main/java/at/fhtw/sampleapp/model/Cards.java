package at.fhtw.sampleapp.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cards {


    @JsonAlias({"Id"})
    private String card_id;

    @JsonAlias({"Name"})
    private String name;

    @JsonAlias({"Damage"})
     private Integer damage;

    private String element;

    private String type;

    // Jackson needs the default constructor
    public Cards(){}
    public Cards(String card_id, String name, Integer damage) {
        this.card_id = card_id;
        this.name = name;
        this.damage = damage;
        String[] tmp = name.split("(?=\\p{Upper})", 2);
        if(tmp.length == 1){
            this.element = "Normal";  //better null?
            this.type = tmp[0];
        } else {
            this.element = tmp[0];
            if(this.element.equals("Regular")){
                this.element = "Normal";
            }
            this.type = tmp[1];
        }

    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        String[] tmp = name.split("(?=\\p{Upper})", 2);
        if(tmp.length == 1){
            this.element = "Normal";  //better null?
            this.type = tmp[0];
        } else {
            this.element = tmp[0];
            if(this.element.equals("Regular")){
                this.element = "Normal";
            }
            this.type = tmp[1];
        }
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

}
