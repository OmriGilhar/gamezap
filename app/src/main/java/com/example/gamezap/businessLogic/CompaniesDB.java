package com.example.gamezap.businessLogic;
import com.example.gamezap.R;


import java.util.ArrayList;

public class CompaniesDB {
    public static ArrayList<Company> Companies() {
        ArrayList<Company> companies = new ArrayList<>();

        companies.add(new Company(
                "Steam",
                "https://store.steampowered.com/",
                R.drawable._280px_steam_icon_logo
        ));

        companies.add(new Company(
                "Epic",
                "https://www.epicgames.com/",
                R.drawable.epic_games_logo
        ));

        companies.add(new Company(
                "GOG",
                "https://www.gog.com/",
                R.drawable.gog_space
        ));
        companies.add(new Company(
                "Ubisoft",
                "https://www.ubisoft.com/",
                R.drawable.ubisoft_logo
        ));

        return companies;
    }
}
