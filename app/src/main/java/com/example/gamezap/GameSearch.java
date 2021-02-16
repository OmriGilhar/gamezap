package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

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
import com.example.gamezap.businessLogic.Adapter_Game;
import com.example.gamezap.businessLogic.SteamFeature;
import com.example.gamezap.businessLogic.User;
import com.example.gamezap.network.RequestQueueSingleton;
import com.example.gamezap.utils.SteamJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameSearch extends AppCompatActivity {
    private User user;
    private RequestQueue requestQueue;
    private List<SteamFeature> steamFeatures;
    private Adapter_Game adapter_games;
    private RecyclerView gameSearch_RCY_topSellers;
    private RecyclerView gameSearch_RCY_newReleases;
    private RecyclerView gameSearch_RCY_comingSoon;
    private CircleImageView gameSearch_IMG_profile;
    private SearchView search_SRV_search;
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
        gameSearch_RCY_topSellers = findViewById(R.id.gameSearch_RCY_topSellers);
        gameSearch_RCY_newReleases = findViewById(R.id.gameSearch_RCY_newReleases);
        gameSearch_RCY_comingSoon = findViewById(R.id.gameSearch_RCY_comingSoon);
        gameSearch_IMG_profile = findViewById(R.id.gameSearch_IMG_profile);
        search_SRV_search = findViewById(R.id.search_SRV_search);
        gameSearch_BTN_search = findViewById(R.id.gameSearch_BTN_search);
        gameSearch_BTN_random = findViewById(R.id.gameSearch_BTN_random);
    }

    private void initViews() {
        getUserDetails();

        // Set the horizontal layout manager as the layout manager of the recycle views.
        gameSearch_RCY_topSellers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gameSearch_RCY_newReleases.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        gameSearch_RCY_comingSoon.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        setProfile();
        setupNetwork();
        requestGames();
        initSearchBar();
    }

    private void setupNetwork() {

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 2048 * 2048); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        this.requestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        // Start the queue
        this.requestQueue.start();

    }

    private void requestGames() {
        String STEAM_URL = "https://store.steampowered.com/api/featuredcategories/";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, STEAM_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            steamFeatures = SteamJsonParser.parseSteamFeatured(response);
                            fillGameSliders();
                        } catch (JSONException e) {
                            Log.println(Log.ERROR, "Error", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.println(Log.INFO, "asdasd", "Request Error" + error.toString());
                    }
                });



        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void initSearchBar() {
        search_SRV_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // do something on text submit, send FB query with the game name.

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void searchGame(){
        CharSequence query = search_SRV_search.getQuery();
        search_SRV_search.setQueryHint("Search Game...");
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
        fillTopSellers();
        fillLessNewReleases();
        fillComingSoon();
    }

    private void fillComingSoon() {
        // Mock games and insert them to the recycle view
        this.adapter_games = new Adapter_Game(this, steamFeatures.get(1).getGames(), this.user);
        gameSearch_RCY_comingSoon.setAdapter(adapter_games);
    }

    private void fillLessNewReleases() {
        // Mock games and insert them to the recycle view
        this.adapter_games = new Adapter_Game(this, steamFeatures.get(3).getGames(), this.user);
        gameSearch_RCY_newReleases.setAdapter(adapter_games);
    }

    private void fillTopSellers() {
        // Mock games and insert them to the recycle view
        this.adapter_games = new Adapter_Game(this, steamFeatures.get(0).getGames(), this.user);
        gameSearch_RCY_topSellers.setAdapter(adapter_games);
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