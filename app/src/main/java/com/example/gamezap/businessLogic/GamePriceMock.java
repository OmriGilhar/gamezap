package com.example.gamezap.businessLogic;

import java.util.ArrayList;

public class GamePriceMock {
    public static ArrayList<GamePrice> Companies() {
        ArrayList<GamePrice> gamePrices = new ArrayList<>();
        ArrayList<Game> games = GameMock.generateGames();
        gamePrices.add(new GamePrice(
                games.get(0),
                CompanyDB.Companies().stream()
                        .filter(company -> company.getName().equals("Steam"))
                        .findAny()
                        .orElse(null),
                50.00
        ));

        gamePrices.add(new GamePrice(
                games.get(0),
                CompanyDB.Companies().stream()
                        .filter(company -> company.getName().equals("Epic"))
                        .findAny()
                        .orElse(null),
                50.00
        ));

        gamePrices.add(new GamePrice(
                games.get(0),
                CompanyDB.Companies().stream()
                        .filter(company -> company.getName().equals("GOG"))
                        .findAny()
                        .orElse(null),
                50.00
        ));

        gamePrices.add(new GamePrice(
                games.get(0),
                CompanyDB.Companies().stream()
                        .filter(company -> company.getName().equals("Ubisoft"))
                        .findAny()
                        .orElse(null),
                50.00
        ));

        return gamePrices;
    }
}
