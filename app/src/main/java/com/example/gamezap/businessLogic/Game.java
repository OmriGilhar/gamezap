package com.example.gamezap.businessLogic;

public class Game {
    private String name;
    private String imageLink;
    private double price;
    private String description;
    private String releaseDate;

    public Game() { }

    public Game(String name, String imageLink, double price, String description, String releaseDate){
        this.setName(name);
        this.setImageLink(imageLink);
        this.setPrice(price);
        this.setDescription(description);
        this.setReleaseDate(releaseDate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
