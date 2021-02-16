package com.example.gamezap.businessLogic;

public class GamePrice {
    private Game game;
    private Company company;
    private String price;

    public GamePrice() {
    }

    public GamePrice(Game game, Company company, String price) {
        this.game = game;
        this.company = company;
        this.price = price;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
