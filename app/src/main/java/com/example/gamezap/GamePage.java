package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.gamezap.businessLogic.Adapter_GamePrice;
import com.example.gamezap.businessLogic.CompaniesDB;
import com.example.gamezap.businessLogic.Company;
import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.GamePrice;
import com.example.gamezap.businessLogic.User;
import com.example.gamezap.network.RequestQueueSingleton;
import com.example.gamezap.utils.SteamJsonParser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class GamePage extends AppCompatActivity {

    private Game game;
    private User user;
    private List<GamePrice> gamePrices = new ArrayList<>();
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
                initFavoriteIcon();
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
    }

    private void initRecycleView() {
        Adapter_GamePrice adapter_gamesPrice = new Adapter_GamePrice(this, gamePrices);
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
            // TODO: Update FB User
            setFavoriteIcon();
        });
    }

    private void initGameViews() {
        setupNetwork();
        requestGame();
    }

    private void requestGame() {
        // Fixed URL using Steam web api to get content to sliders.
        String STEAM_GAME_URL = "https://store.steampowered.com/api/appdetails/?appids=" + this.game.getId();
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, STEAM_GAME_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse feature category response --> List<SteamFeature>
                            List<String> gameExtras = SteamJsonParser.parseDataToGamePage(response, game.getId());
                            game.setDescription(gameExtras.get(0));
                            game.setReleaseDate(gameExtras.get(1));
                            createGamePrices(gameExtras.get(2), (Double.parseDouble(gameExtras.get(3))/100));
                            setGameCover();
                            setGameName();
                            setGameDescription();
                            setGameReleaseDate();
                            initRecycleView();
                        } catch (JSONException e) {
                            Log.println(Log.ERROR, "Error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.println(Log.ERROR, "asdasd", "Request Error" + error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @SuppressLint("DefaultLocale")
    private void createGamePrices(String currency, double price) {
        double numToRand = 0;
        String steamPrice = price + " " + currency;
        for(Company comp: CompaniesDB.Companies()){
            if(comp.getName().equals("Steam")){
                this.gamePrices.add(new GamePrice(this.game, comp, steamPrice));
            }
            else {
                if (price > 11.0) {
                    numToRand = (price - 5) + ((price + 5) - (price - 5)) * new Random().nextDouble();
                } else {
                    numToRand = price;
                }
                steamPrice = String.format("%.2f", numToRand) + " " + currency;
                this.gamePrices.add(new GamePrice(this.game, comp, steamPrice));
            }
        }
    }

    private void setupNetwork() {

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 2048 * 2048); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        // Start the queue
        requestQueue.start();

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
           gamePage_TXT_favorite.setText("Remove from Favorite");
       }else{
           gamePage_IMGB_favorite.setImageResource(R.drawable.add_fav_star);
           gamePage_TXT_favorite.setText("Add to Favorite");
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