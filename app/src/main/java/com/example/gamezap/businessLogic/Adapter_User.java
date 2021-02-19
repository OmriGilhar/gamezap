package com.example.gamezap.businessLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_User {
    public static Map<String, Object> serializeUserFB(User user){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", user.getUserName());
        userMap.put("email", user.getEmail());
        userMap.put("creationTime", user.getCreationTime());
        userMap.put("pictureURL", user.getPictureURL());
        userMap.put("favoriteGames", user.getFavoriteGames());
        return userMap;
    }
}
