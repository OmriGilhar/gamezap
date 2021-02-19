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
import com.example.gamezap.businessLogic.User;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


public class ProfilePage extends AppCompatActivity {

    private User user;
    private ImageView profile_IMG_profile;
    private TextView profile_TXT_userName;
    private TextView profile_TXT_email;
    private TextView profile_TXT_favorite;
    private RecyclerView profile_RCY_favorite_left;
    private RecyclerView profile_RCY_favorite_right;

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
        data.putExtra("userDetails", this.user);
        setResult(RESULT_OK, data);
        super.finish();
    }

    private void findViews() {
        profile_IMG_profile = findViewById(R.id.profile_IMG_profile);
        profile_TXT_userName = findViewById(R.id.profile_TXT_userName);
        profile_TXT_email = findViewById(R.id.profile_TXT_email);
        profile_TXT_favorite = findViewById(R.id.profile_TXT_favorite);
        profile_RCY_favorite_left = findViewById(R.id.profile_RCY_favorite_left);
        profile_RCY_favorite_right = findViewById(R.id.profile_RCY_favorite_right);
    }

    private void initViews() {
        getUserDetails();
        setProfilePicture();
        setProfileData();
        profile_RCY_favorite_left.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        profile_RCY_favorite_right.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
        Adapter_Game adapter_left = null;
        Adapter_Game adapter_right = null;
        if(this.user.getFavoriteGames().size() % 2 == 0){
            List<List<Game>> lists = Lists.partition(this.user.getFavoriteGames(), (this.user.getFavoriteGames().size() + 1) / 2);
            adapter_left = new Adapter_Game(this, lists.get(0) , user);
            adapter_right = new Adapter_Game(this, lists.get(1) , user);

        }
        else if(this.user.getFavoriteGames().size() > 0){
            List<Game> gameListLeft = new ArrayList<>();
            List<Game> gameListRight = new ArrayList<>();
            int leftList = this.user.getFavoriteGames().size() / 2;
            int counter = 0;
            for(int i=0; i < this.user.getFavoriteGames().size(); i++){
                if(counter <= leftList){
                    gameListLeft.add(this.user.getFavoriteGames().get(i));
                    counter++;
                }else{
                    gameListRight.add(this.user.getFavoriteGames().get(i));
                }
            }
            adapter_left = new Adapter_Game(this, gameListLeft , user);
            adapter_right = new Adapter_Game(this, gameListRight , user);

        }
        profile_RCY_favorite_left.setAdapter(adapter_left);
        profile_RCY_favorite_right.setAdapter(adapter_right);
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