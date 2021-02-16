package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamezap.businessLogic.Adapter_Game;
import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.GameMock;
import com.example.gamezap.businessLogic.User;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {

    private User user;
    private ImageView profile_IMG_profile;
    private TextView profile_TXT_userName;
    private TextView profile_TXT_email;
    private TextView profile_TXT_favorite;
    private RecyclerView profile_RCY_favorite_games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        findViews();
        initViews();

    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("userDetails", user);
        setResult(RESULT_OK, data);
        super.finish();
    }

    private void findViews() {
        profile_IMG_profile = findViewById(R.id.profile_IMG_profile);
        profile_TXT_userName = findViewById(R.id.profile_TXT_userName);
        profile_TXT_email = findViewById(R.id.profile_TXT_email);
        profile_TXT_favorite = findViewById(R.id.profile_TXT_favorite);
        profile_RCY_favorite_games = findViewById(R.id.profile_RCY_favorite_games);
    }

    private void initViews() {
        getUserDetails();
        setProfilePicture();
        setProfileData();
        profile_RCY_favorite_games.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setFavoriteGames();
    }

    private void getUserDetails() {
        this.user = getIntent().getParcelableExtra("userDetails");
    }

    private void setProfilePicture() {
        // Load users profile picture
        Glide
                .with(this)
                .load(this.user.getPictureURL())
                .into(profile_IMG_profile);
    }

    private void setProfileData() {
        // Set profile user name
        profile_TXT_userName.setText(user.getUserName());

        // Set profile user email
        profile_TXT_email.setText(user.getEmail());

        // Set profile user favorite games amount
        profile_TXT_favorite.setText(getString(R.string.blank, user.getFavoriteGames().size()));
    }

    private void setFavoriteGames(){
        Adapter_Game adapter_games = new Adapter_Game(this, this.user.getFavoriteGames(), user);
        profile_RCY_favorite_games.setAdapter(adapter_games);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                this.user = data.getParcelableExtra("userDetails");
                setProfileData();
                setFavoriteGames();
            }
        }
    }
}