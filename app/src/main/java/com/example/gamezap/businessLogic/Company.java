package com.example.gamezap.businessLogic;

import android.graphics.drawable.Drawable;

public class Company {
    private String name;
    private String URL;
    private int logo;

    public Company() {
    }

    public Company(String name, String URL, int logo) {
        this.name = name;
        this.URL = URL;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
