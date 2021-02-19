package com.example.gamezap.businessLogic;

import java.util.ArrayList;
import java.util.List;

public class SteamFeature {
    private String id;
    private String name;
    private List<Game> games;

    public SteamFeature() {
    }

    public SteamFeature(String id, String name, List<Game> games) {
        this.id = id;
        this.name = name;
        this.games = games;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }
}
