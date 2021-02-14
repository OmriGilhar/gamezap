package com.example.gamezap.businessLogic;
import com.bumptech.glide.Glide;
import com.example.gamezap.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

public class Adapter_Game extends RecyclerView.Adapter<Adapter_Game.MyViewHolder> {

    private List<Game> games;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Game(Context context, List<Game> games) {
        this.mInflater = LayoutInflater.from(context);
        this.games = games;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_games, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Game game = games.get(position);
        Glide
                .with(mInflater.getContext())
                .load(game.getImageLink())
                .into(holder.game_IMG_cover);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return games.size();
    }
    
    // convenience method for getting data at click position
    Game getItem(int id) {
        return games.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends ViewHolder implements View.OnClickListener {
        ImageView game_IMG_cover;

        MyViewHolder(View itemView) {
            super(itemView);
            game_IMG_cover = itemView.findViewById(R.id.game_IMG_cover);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}
