package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.gamezap.businessLogic.Adapter_Game;
import com.example.gamezap.businessLogic.Adapter_User;
import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.GameMock;
import com.example.gamezap.businessLogic.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameSearch extends AppCompatActivity {
    private User user;
    private Adapter_Game adapter_games;
    private RecyclerView gameSearch_RCY_topGames;
    private RecyclerView gameSearch_RCY_UnderPrice;
    private RecyclerView gameSearch_RCY_general;
    private CircleImageView gameSearch_IMG_profile;
    private EditText search_ETX_search;
    private Button gameSearch_BTN_search;
    private Button gameSearch_BTN_random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        findViews();
        initViews();
    }

    private void findViews() {
        gameSearch_RCY_topGames = findViewById(R.id.gameSearch_RCY_topGames);
        gameSearch_RCY_UnderPrice = findViewById(R.id.gameSearch_RCY_UnderPrice);
        gameSearch_RCY_general = findViewById(R.id.gameSearch_RCY_general);
        gameSearch_IMG_profile = findViewById(R.id.gameSearch_IMG_profile);
        search_ETX_search = findViewById(R.id.search_ETX_search);
        gameSearch_BTN_search = findViewById(R.id.gameSearch_BTN_search);
        gameSearch_BTN_random = findViewById(R.id.gameSearch_BTN_random);
    }

    private void initViews() {
        getUserDetails();

        // Set the horizontal layout manager as the layout manager of the recycle views.
        gameSearch_RCY_topGames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gameSearch_RCY_UnderPrice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gameSearch_RCY_general.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setProfile();
        fillGameSliders();
    }

    private void setProfile() {
        // Load users profile picture
        Glide
            .with(this)
            .load(this.user.getPictureURL())
            .into(gameSearch_IMG_profile);

        // When user wants to move to his profile page.
        gameSearch_IMG_profile.setOnClickListener(view -> {
            Intent profileActivity = new Intent(GameSearch.this, ProfilePage.class);

            profileActivity.putExtra("userDetails", this.user);

            GameSearch.this.startActivityForResult(profileActivity, RESULT_OK);
        });
    }

    private void fillGameSliders() {
        fillTopGames();
        fillLessThenGames();
        fillGeneralGames();
    }

    private void fillGeneralGames() {
    }

    private void fillLessThenGames() {
    }

    private void fillTopGames() {
        // Mock games and insert them to the recycle view
        ArrayList<Game> games = GameMock.generateGames();

        this.adapter_games = new Adapter_Game(this, games, this.user);
        gameSearch_RCY_topGames.setAdapter(adapter_games);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                this.user = data.getParcelableExtra("userDetails");
                this.adapter_games.setUser(this.user);
            }
        }
    }

    private void getUserDetails() {
        // *** MOCKING USER ***
        this.user = getIntent().getParcelableExtra("userDetails");
    }

}