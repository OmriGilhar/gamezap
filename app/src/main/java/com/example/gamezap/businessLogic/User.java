package com.example.gamezap.businessLogic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class User implements Parcelable {

    private String userName;
    private String email;
    private String uuid;
    private String creationTime;
    private String password;
    private String pictureURL;
    private ArrayList<Game> favoriteGames;

    public User() {
    }

    public User(String userName, String email, String password) {
        this.setUserName(userName);
        this.setEmail(email);
        this.setUuid(UUID.randomUUID().toString().replace("-", ""));
        this.setCreationTime(
                new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.getDefault())
                        .format(Calendar.getInstance().getTime()));
        this.setPassword(password);
        this.setPictureURL("");
        this.setFavoriteGames(new ArrayList<>());
    }

    public User(String userName, String email, String uuid, String creationTime, String password, String pictureURL, ArrayList<Game> favoriteGames) {
        this.setEmail(email);
        this.setUuid(uuid);
        this.setCreationTime(creationTime);
        this.setPassword(password);
        this.setPassword(pictureURL);
        this.setFavoriteGames(favoriteGames);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPictureURL() {
        if(this.pictureURL.equals("")){
            return "https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png";
        }
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public ArrayList<Game> getFavoriteGames() {
        return favoriteGames;
    }

    public void setFavoriteGames(ArrayList<Game> favoriteGames) {
        this.favoriteGames = favoriteGames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected User(Parcel in) {
        this.setUserName(in.readString());
        this.setEmail(in.readString());
        this.setUuid(in.readString());
        this.setCreationTime(in.readString());
        this.setPassword(in.readString());
        this.setPictureURL(in.readString());
        ArrayList<Game> favoriteGames = new ArrayList<>();
        in.readList(favoriteGames, Game.class.getClassLoader());
        this.setFavoriteGames(favoriteGames);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getUserName());
        dest.writeString(this.getEmail());
        dest.writeString(this.getUuid());
        dest.writeString(this.getCreationTime());
        dest.writeString(this.getPassword());
        dest.writeString(this.getPictureURL());
        dest.writeList(this.getFavoriteGames());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
