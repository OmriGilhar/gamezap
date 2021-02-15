package com.example.gamezap.businessLogic;

import java.util.ArrayList;

public class Adapter_User {
    public User serializeUserFB(
            String userName,
            String email,
            String uuid,
            String creationTime,
            String password,
            String pictureURL,
            ArrayList<Game> favoriteGames
    ){
        return new User(userName, email, uuid, creationTime, password, pictureURL, favoriteGames);
    }

    // This is just a mock of deserialize a user to a FB format.
    public String deserialize(User user){
        StringBuilder games = new StringBuilder();
        for(Game game: user.getFavoriteGames()){
            games.append(game.getName()).append(", ");
        }

        return  "username = " + user.getUserName() + ", " +
                "Email = " + user.getEmail() + ", " +
                "uuid = " + user.getUuid() + ", " +
                "creation time = " + user.getCreationTime() + ", " +
                "password = " + user.getPassword() + ", " +
                "picture URL = " + user.getPictureURL() + ", " +
                "Favorite Games = " + games.toString();
    }
}
