package com.example.gamezap.businessLogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamezap.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Sliders extends RecyclerView.Adapter<Adapter_Sliders.SliderViewHolder>{

    private List<SteamFeature> features;
    private List<Adapter_Game> adapters = new ArrayList<>();
    private LayoutInflater mInflater;
    private Adapter_Game.ItemClickListener mClickListener;
    private User user;

    // data is passed into the constructor
    public Adapter_Sliders(Context context, List<SteamFeature> features, User user) {
        this.mInflater = LayoutInflater.from(context);
        this.features = features;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
        for(Adapter_Game adapter: this.adapters){
            adapter.setUser(user);
        }
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_sliders, parent, false);
        return new SliderViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(SliderViewHolder holder, int position) {
        SteamFeature feature = features.get(position);

        holder.slider_TXT_sliderTitle.setText(feature.getName());

        // Set recycle in the slider
        Adapter_Game gameAdapter = new Adapter_Game(mInflater.getContext(), feature.getGames(), this.user);
        adapters.add(gameAdapter);
        holder.slider_RCY_sliderGames.setAdapter(gameAdapter);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return features.size();
    }

    // convenience method for getting data at click position
    SteamFeature getItem(int id) {
        return features.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(Adapter_Game.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class SliderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView slider_RCY_sliderGames;
        TextView slider_TXT_sliderTitle;

        SliderViewHolder(View itemView) {
            super(itemView);
            slider_RCY_sliderGames = itemView.findViewById(R.id.slider_RCY_sliderGames);
            slider_RCY_sliderGames.setLayoutManager(new LinearLayoutManager(mInflater.getContext(), LinearLayoutManager.HORIZONTAL, false));
            slider_TXT_sliderTitle = itemView.findViewById(R.id.slider_TXT_sliderTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
