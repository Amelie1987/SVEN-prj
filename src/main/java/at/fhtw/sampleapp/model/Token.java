package at.fhtw.sampleapp.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Token {

    @JsonAlias({"Authorization"})
    private String token_id;

    // Jackson needs the default constructor
    public Token(){}

    public Token(String token_id) {
        this.token_id = token_id;

    }
    public String getUsername() {
        String tmp[] = this.token_id.split(" ", 2);
        String username[] = tmp[1].split("-",2);
        return username[0];
    }
    public Boolean isAdmin(){
        String tmp[] = this.token_id.split(" ", 2);
        String isAdmin[] = tmp[1].split("-",2);
        if(isAdmin[0].equals("admin")){
            System.out.println("Token accepted from " + isAdmin[0]);
            return true;
        }
        return false;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }
}
