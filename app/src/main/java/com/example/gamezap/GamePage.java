package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamezap.businessLogic.Adapter_GamePrice;
import com.example.gamezap.businessLogic.Adapter_User;
import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.User;


import de.hdodenhof.circleimageview.CircleImageView;

public class GamePage extends AppCompatActivity {

    private Game game;
    private User user;
    private CircleImageView gamePage_IMG_profile;
    private ImageButton gamePage_IMGB_favorite;
    private TextView gamePage_TXT_favorite;
    private ImageView gamePage_IMG_gameCover;
    private TextView gamePage_TXT_name;
    private TextView gamePage_TXT_gameDes;
    private TextView gamePage_TXT_releaseData;
    private RecyclerView gamePage_RCY_Companies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                this.user = data.getParcelableExtra("userDetails");
            }
        }
    }


    private void findViews() {
        gamePage_IMG_profile = findViewById(R.id.gamePage_IMG_profile);
        gamePage_IMGB_favorite = findViewById(R.id.gamePage_IMGB_favorite);
        gamePage_TXT_favorite = findViewById(R.id.gamePage_TXT_favorite);
        gamePage_IMG_gameCover = findViewById(R.id.gamePage_IMG_gameCover);
        gamePage_TXT_name = findViewById(R.id.gamePage_TXT_name);
        gamePage_TXT_gameDes = findViewById(R.id.gamePage_TXT_gameDes);
        gamePage_TXT_releaseData = findViewById(R.id.gamePage_TXT_releaseData);
        gamePage_RCY_Companies = findViewById(R.id.gamePage_RCY_Companies);
    }

    private void initViews() {
        getGameDetails();
        getUserDetails();
        setProfile();
        initFavoriteIcon();
        initGameViews();
        initRecycleView();
    }

    private void initRecycleView() {
        Adapter_GamePrice adapter_gamesPrice = new Adapter_GamePrice(this, game);
        gamePage_RCY_Companies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        gamePage_RCY_Companies.setAdapter(adapter_gamesPrice);
    }

    private void initFavoriteIcon() {
        setFavoriteIcon();
        gamePage_IMGB_favorite.setOnClickListener(v -> {
            if(isFavorite()){
                for(int i=0; i < user.getFavoriteGames().size(); i++) {
                    if (user.getFavoriteGames().get(i).getName().equals(this.game.getName())){
                        user.getFavoriteGames().remove(i);
                    }
                }
            }else{
                user.getFavoriteGames().add(game);
            }
            setFavoriteIcon();
        });
    }

    private void initGameViews() {
        setGameCover();
        setGameName();
        setGameDescription();
        setGameReleaseDate();
    }

    private void setGameCover() {
        Glide
                .with(this)
                .load(game.getImageLink())
                .into(gamePage_IMG_gameCover);
    }

    private void setGameName() {
        gamePage_TXT_name.setText(game.getName());
    }

    private void setGameDescription() {
        gamePage_TXT_gameDes.setMovementMethod(new ScrollingMovementMethod());
        gamePage_TXT_gameDes.setText(game.getDescription());
    }

    private void setGameReleaseDate() {
        gamePage_TXT_releaseData.setText(game.getReleaseDate());
    }

    private void setFavoriteIcon() {
       if(isFavorite()){
           gamePage_IMGB_favorite.setImageResource(R.drawable.remove_fav_star);
           gamePage_TXT_favorite.setText("Remove From Favorite");
       }else{
           gamePage_IMGB_favorite.setImageResource(R.drawable.add_fav_star);
           gamePage_TXT_favorite.setText("Add From Favorite");
       }
    }

    private boolean isFavorite(){
        for(Game game: user.getFavoriteGames()){
            if(game.getName().equals(this.game.getName())){
                return true;
            }
        }
        return false;
    }

    private void setProfile() {
        // Load users profile picture
        Glide
                .with(this)
                .load(this.user.getPictureURL())
                .into(gamePage_IMG_profile);

        // When user wants to move to his profile page.
        gamePage_IMG_profile.setOnClickListener(view -> {
            Intent profileActivity = new Intent(GamePage.this, ProfilePage.class);

            profileActivity.putExtra("userDetails", this.user);

            GamePage.this.startActivity(profileActivity);
        });
    }

    private void getUserDetails() {
        this.user = getIntent().getParcelableExtra("userDetails");
    }

    private void getGameDetails(){
        this.game = getIntent().getParcelableExtra("gameDetails");
    }
}