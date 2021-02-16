package com.example.gamezap.utils;

import android.util.Log;

import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.SteamFeature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SteamJsonParser {

    public static List<SteamFeature> parseSteamFeatured(JSONObject json) throws JSONException {
        String[] steamFeaturedTitles = {"top_sellers", "coming_soon", "specials", "new_releases", "specials"};
        List<SteamFeature> list = new ArrayList<>();

        for(int i = 0 ; i < steamFeaturedTitles.length ; i++){
            try{
                    JSONObject feature = json.getJSONObject(steamFeaturedTitles[i]);
                    JSONArray games = feature.getJSONArray("items");
                    List<Game> gameList = new ArrayList<>();
                    for(int j=0; j < games.length(); j++){
                        JSONObject gameDetails = games.getJSONObject(j);
                        gameList.add(new Game(
                                gameDetails.getInt("id"),
                                gameDetails.getString("name"),
                                gameDetails.getString("large_capsule_image"),
                                "TODO!",
                                "TODO!"
                        ));
                    }
                    Log.println(Log.INFO,"asdasd", gameList.get(0).getName());
                    list.add(new SteamFeature(
                            feature.getString("id"),
                            feature.getString("name"),
                            gameList
                        )
                    );
            }catch (Exception e){
                Log.println(Log.ERROR,"asdasd", e.toString());
            }
        }
        return list;
    }
}
