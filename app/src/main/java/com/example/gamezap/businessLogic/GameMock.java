package com.example.gamezap.businessLogic;

import java.util.ArrayList;

public class GameMock {

    public static ArrayList<Game> generateGames() {
        ArrayList<Game> games = new ArrayList<>();

        games.add(new Game(
                "Borderlands 3",
                "https://www.posterarthouse.com/media/catalog/product/cache/c687aa7517cf01e65c009f6943c2b1e9/1/6/160886.jpg",
                79.20,
                "The original shooter-looter returns, packing bazillions of guns and a mayhem-fueled adventure! Blast through new worlds & enemies and save your home from the most ruthless cult leaders in the galaxy.",
                "13 Mar, 2020"
        ));

        games.add(new Game(
                        "Rust",
                        "https://store.speedtree.com/site-assets/uploads/Rust-cover.jpg",
                        100.46,
                        "The only aim in Rust is to survive - Overcome struggles such as hunger, thirst and cold. Build a fire. Build a shelter. Kill animals. Protect yourself from other players.",
                        "8 Feb, 2018"
        ));

        games.add(new Game(
                "Little Nightmares II",
                "https://s3.gaming-cdn.com/images/products/5381/orig/little-nightmares-ii-cover.jpg",
                129.00,
                "Little Nightmares II is a suspense adventure game in which you play as Mono, a young boy trapped in a world that has been distorted by an evil transmission. Together with new friend Six, he sets out to discover the source of the Transmission.",
                "11 Feb, 2021"
        ));

        games.add(new Game(
                "Red Dead Redemption 2",
                "https://assets.vg247.com/current//2018/05/red_dead_redemption_2_cover_art_1.jpg",
                154.10,
                "Little Nightmares II is a suspense adventure game in which you play as Mono, a young boy trapped in a world that has been distorted by an evil transmission. Together with new friend Six, he sets out to discover the source of the Transmission.",
                "5 Dec, 2019"
        ));

        games.add(new Game(
                "CyberPunk 2077",
                "https://s1.gaming-cdn.com/images/products/840/orig/cyberpunk-2077-cover.jpg",
                242.10,
                "Cyberpunk 2077 is an open-world, action-adventure story set in Night City, a megalopolis obsessed with power, glamour and body modification. You play as V, a mercenary outlaw going after a one-of-a-kind implant that is the key to immortality.",
                "10 Dec, 2020"
        ));

        games.add(new Game(
                "Counter-Strike: Global Offensive",
                "https://static.wikia.nocookie.net/cswikia/images/1/1e/Csgo_steam_store_header_latest.jpg/revision/latest?cb=20170630034202",
                0,
                "Counter-Strike: Global Offensive (CS: GO) expands upon the team-based action gameplay that it pioneered when it was launched 19 years ago. CS: GO features new maps, characters, weapons, and game modes, and delivers updated versions of the classic CS content (de_dust2, etc.).",
                "21 Aug, 2012"
        ));


        return games;
    }



}
