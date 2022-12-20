package at.fhtw.sampleapp.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Users {

    @JsonAlias({"Id"})
    private Integer id;

    @JsonAlias({"Username"})
    private String username;

    @JsonAlias({"Password"})
    private String password;

    @JsonAlias({"Name"})
    private String name;

    @JsonAlias({"Bio"})
    private String bio;

    @JsonAlias({"Image"})
    private String image;

    @JsonAlias({"Elo"})
    private Integer elo;

    @JsonAlias({"Wins"})
    private Integer wins;

    @JsonAlias({"Losses"})
    private Integer losses;

    @JsonAlias({"Coins"})
    private Integer coins;

    // Jackson needs the default constructor
    public Users() {}

    public Users(Integer id, String username, String password, String name, String bio, String image, Integer elo, Integer wins, Integer losses, Integer coins) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
        this.coins = coins;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public Integer getElo() {
        return elo;
    }

    public Integer getWins() {
        return wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public Integer getCoins() {
        return coins;
    }
}
