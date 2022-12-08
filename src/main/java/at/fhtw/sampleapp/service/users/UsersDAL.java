package at.fhtw.sampleapp.service.users;

import at.fhtw.sampleapp.model.Users;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;


import java.util.ArrayList;
import java.util.List;

public class UsersDAL {


    private List<Users> usersData;
    public UsersDAL() {
        usersData = new ArrayList<>();
        usersData.add(new Users("6cm85277-4594-49d4-b0cf-ba0a921faad0", "21342134", "sichersuper", "Bob", "its me", ":-)", 200, 0, 0, 20));
        usersData.add(new Users("94d87277-4590-49d4-b0cf-ba0a903faad1", "12341234", "supersicher", "Bobby", "its bobby", "xD", 200, 0, 0, 20));
        //usersData.add(new Users(3,"Tokyo", 12.f));

    }

    public Users getUsersbyUsername(String username){
        Users foundUsers = usersData.stream()
                //.filter(users -> id == users.getId())
                .filter(users -> username.equals(users.getUsername()) == true)
                .findAny()
                .orElse(null);

        return foundUsers;
    }

    {



/*
    // GET /users/:id
    public Users getUsers(String id) {
        Users foundUsers = usersData.stream()
                //.filter(users -> id == users.getId())
                .filter(users -> id.equals(users.getId()) == 1)
                .findAny()
                .orElse(null);

    return foundUsers;
    }

    // GET /users
    public List<Users> getUsers() {
        return usersData;
    }

    // POST /users
    public void addUsers(Users users) {
        usersData.add(users);
    }   */
    }

}