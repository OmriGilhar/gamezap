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

    public static List<SteamFeature> parseSteamFeatured(JSONObject json){
        String[] steamFeaturedTitles = {"top_sellers", "coming_soon", "specials", "new_releases", "specials"};
        List<SteamFeature> list = new ArrayList<>();

        for (String steamFeaturedTitle : steamFeaturedTitles) {
            try {
                JSONObject feature = json.getJSONObject(steamFeaturedTitle);
                JSONArray games = feature.getJSONArray("items");
                List<Game> gameList = new ArrayList<>();
                for (int j = 0; j < games.length(); j++) {
                    JSONObject gameDetails = games.getJSONObject(j);
                    if (gameDetails.getInt("type") == 0) {
                        gameList.add(new Game(
                                gameDetails.getInt("id"),
                                gameDetails.getString("name"),
                                gameDetails.getString("large_capsule_image"),
                                "TODO!",
                                "TODO!"
                        ));
                    }
                }
                list.add(new SteamFeature(
                                feature.getString("id"),
                                feature.getString("name"),
                                gameList
                        )
                );
            } catch (Exception e) {
                Log.println(Log.ERROR, "SteamJsonParser:Error", e.toString());
            }
        }
        return list;
    }

    public static List<String> parseDataToGamePage(JSONObject json, int gameId) throws JSONException {
        List<String> gameDetail = new ArrayList<>();
        JSONObject game = json.getJSONObject(String.valueOf(gameId));
        JSONObject data = game.getJSONObject("data");
        JSONObject releaseDate = data.getJSONObject("release_date");

        gameDetail.add(data.getString("short_description"));
        gameDetail.add(releaseDate.getString("date"));
        try{
            JSONObject priceOverview = data.getJSONObject("price_overview");
            gameDetail.add(priceOverview.getString("currency"));
            gameDetail.add(String.valueOf(priceOverview.getInt("final")));
        }catch (org.json.JSONException je){
            gameDetail.add(" ");
            gameDetail.add("0.00");
        }

        return gameDetail;
    }

    public static Game parseSteamGame(JSONObject json, int id) throws JSONException {
        JSONObject game = json.getJSONObject(String.valueOf(id));
        JSONObject data = game.getJSONObject("data");
        JSONObject releaseDate = data.getJSONObject("release_date");

        return new Game(
                id,
                data.getString("name"),
                data.getString("header_image"),
                data.getString("short_description"),
                releaseDate.getString("date")
            );
    }

}
