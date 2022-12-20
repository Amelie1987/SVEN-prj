package at.fhtw.sampleapp.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Trade {
    @JsonAlias({"Id"})
    private String trade_id;

    @JsonAlias({"CardToTrade"})
    private String card_id;

    @JsonAlias({"Type"})
    private String name;

    @JsonAlias({"MinimumDamage"})
    private Integer minDamage;

    private Integer user_id;

    public Trade(){}

    public Trade(String trade_id,String card_id, String name, Integer minDamage){
        this.trade_id = trade_id;
        this.card_id = card_id;
        this.name = name;
        this.minDamage = minDamage;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
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
    }

    public Integer getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(Integer minDamage) {
        this.minDamage = minDamage;
    }
}
