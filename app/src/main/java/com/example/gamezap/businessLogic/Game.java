package com.example.gamezap.businessLogic;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private int id;
    private String name;
    private String imageLink;
    private String description;
    private String releaseDate;

    public Game() { }

    public Game(int id, String name, String imageLink, String description, String releaseDate){
        this.setId(id);
        this.setName(name);
        this.setImageLink(imageLink);
        this.setDescription(description);
        this.setReleaseDate(releaseDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageLink);
        dest.writeString(description);
        dest.writeString(releaseDate);
    }

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageLink = in.readString();
        description = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };


}
