package com.example.gamezap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.gamezap.businessLogic.Adapter_Game;
import com.example.gamezap.businessLogic.Game;
import com.example.gamezap.businessLogic.GameMock;

import java.util.ArrayList;

public class GameSearch extends AppCompatActivity {

    private RecyclerView gameSearch_RCY_topGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        gameSearch_RCY_topGames = findViewById(R.id.gameSearch_RCY_topGames);

        ArrayList<Game> games = GameMock.generateGames();

        Adapter_Game adapter_games = new Adapter_Game(this, games);
        gameSearch_RCY_topGames.setLayoutManager(new LinearLayoutManager(this));
        gameSearch_RCY_topGames.setAdapter(adapter_games);
        gameSearch_RCY_topGames.setLayoutManager(horizontalLayoutManager);

    }
}