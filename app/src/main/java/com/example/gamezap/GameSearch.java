package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.gamezap.businessLogic.Adapter_Game;
import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.SteamFeature;
import com.example.gamezap.businessLogic.User;
import com.example.gamezap.network.RequestQueueSingleton;
import com.example.gamezap.utils.SteamJsonParser;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameSearch extends AppCompatActivity {
    private User user;
    private List<SteamFeature> steamFeatures;
    private List<Game> gameList;
    private Adapter_Game adapter_comingSoon_games;
    private Adapter_Game adapter_specials_games;
    private Adapter_Game adapter_topSellers_games;
    private RecyclerView gameSearch_RCY_topSellers;
    private RecyclerView gameSearch_RCY_specials;
    private RecyclerView gameSearch_RCY_comingSoon;
    private CircleImageView gameSearch_IMG_profile;
    private SearchView search_SRV_search;
    private ListView gameSearch_LVW_gameList;
    private ArrayAdapter adapter;
    private Button gameSearch_BTN_random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        findViews();
        initViews();
    }

    private void findViews() {
        gameSearch_RCY_topSellers = findViewById(R.id.gameSearch_RCY_topSellers);
        gameSearch_RCY_specials = findViewById(R.id.gameSearch_RCY_specials);
        gameSearch_RCY_comingSoon = findViewById(R.id.gameSearch_RCY_comingSoon);
        gameSearch_IMG_profile = findViewById(R.id.gameSearch_IMG_profile);
        search_SRV_search = findViewById(R.id.search_SRV_search);
        gameSearch_BTN_random = findViewById(R.id.gameSearch_BTN_random);
        gameSearch_LVW_gameList = findViewById(R.id.gameSearch_LVW_gameList);
    }

    private void initViews() {
        getUserDetails();
        initRecycleViews();
        setProfile();
        setupNetwork();
        requestGames();
        initRandomButton();
    }

    private void initRandomButton() {
        gameSearch_BTN_random.setOnClickListener(v -> {
            Game randomGame = gameList.get(new Random().nextInt(gameList.size()));
            requestGameByID(randomGame.getId());
        });

    }

    private void goToRandomGame(Game game) {
        Intent gamePageActivity = new Intent(this, GamePage.class);
        gamePageActivity.putExtra("userDetails", this.user);
        gamePageActivity.putExtra("gameDetails", game);

        this.startActivityForResult(gamePageActivity, 1);
    }

    private void initRecycleViews() {
        // Set the horizontal layout manager as the layout manager of the recycle views.
        gameSearch_RCY_topSellers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gameSearch_RCY_specials.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gameSearch_RCY_comingSoon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    private void setupNetwork() {
        // Instantiate the RequestQueue with the cache and network.
        RequestQueue requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        // Start the queue
        requestQueue.start();

    }

    private void requestGameByID(int id){
        // Fixed URL using Steam web api to get content to sliders.
        String STEAM_GAME_URL = "https://store.steampowered.com/api/appdetails/?appids=" + id;
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, STEAM_GAME_URL, null, response -> {
                    int tries = 0;
                    int randID;
                    while(tries != 10){
                        randID = id + (new Random().nextInt(500 + 1));
                        try {
                            if(tries == 9) {
                                randID = id;
                            }
                            goToRandomGame(SteamJsonParser.parseSteamGame(response, randID));
                            break;
                        } catch (JSONException e) {
                            tries += 1;
                        }
                    }
                }, error -> Log.println(Log.ERROR, "GameSearch:Error", "Request Error" + error.toString()));

        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void requestGames() {
        // Fixed URL using Steam web api to get content to sliders.
        String STEAM_URL = "https://store.steampowered.com/api/featuredcategories/";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, STEAM_URL, null, response -> {
                    // Parse feature category response --> List<SteamFeature>
                    steamFeatures = SteamJsonParser.parseSteamFeatured(response);

                    // Fill sliders with content
                    fillGameSliders();
                    initSearchBar();
                }, error -> Log.println(Log.ERROR, "GameSearch:Error", "Request Error" + error.toString()));

        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void initSearchBar() {
        search_SRV_search.setQueryHint("Search Game...");
        gameList = new ArrayList<>();
        List<String> gameListStrings = new ArrayList<>();
        for(SteamFeature feature: steamFeatures) {
            gameList.addAll(feature.getGames());
        }
        for(Game game: gameList){
            gameListStrings.add(game.getName());
        }
        adapter = new ArrayAdapter<>(GameSearch.this, android.R.layout.simple_list_item_1, gameListStrings);
        gameSearch_LVW_gameList.setAdapter(adapter);
        search_SRV_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(gameListStrings.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(GameSearch.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setProfile() {
        // Load users profile picture
        Glide
            .with(this)
            .load(this.user.getPictureURL())
            .into(gameSearch_IMG_profile);

        // When user wants to move to his profile page.
        gameSearch_IMG_profile.setOnClickListener(view -> moveToProfilePage());
    }

    private void moveToProfilePage(){
        Intent profileActivity = new Intent(this, ProfilePage.class);

        profileActivity.putExtra("userDetails", this.user);

        this.startActivityForResult(profileActivity, 1);
    }

    private void fillGameSliders() {
        fillTopSellers();
        fillSpecials();
        fillComingSoon();
    }

    private void fillComingSoon() {
        this.adapter_comingSoon_games = new Adapter_Game(this, steamFeatures.get(1).getGames(), this.user);
        gameSearch_RCY_comingSoon.setAdapter(adapter_comingSoon_games);
    }

    private void fillSpecials() {
        this.adapter_specials_games = new Adapter_Game(this, steamFeatures.get(4).getGames(), this.user);
        gameSearch_RCY_specials.setAdapter(adapter_specials_games);
    }

    private void fillTopSellers() {
        this.adapter_topSellers_games = new Adapter_Game(this, steamFeatures.get(0).getGames(), this.user);
        gameSearch_RCY_topSellers.setAdapter(adapter_topSellers_games);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                user = data.getParcelableExtra("userDetails");
                this.adapter_comingSoon_games.setUser(user);
                this.adapter_topSellers_games.setUser(user);
                this.adapter_specials_games.setUser(user);
            }
        }
    }

    private void getUserDetails() {
        // *** MOCKING USER ***
        this.user = getIntent().getParcelableExtra("userDetails");
    }

}