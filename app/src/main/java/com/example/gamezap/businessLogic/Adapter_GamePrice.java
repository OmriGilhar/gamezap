package com.example.gamezap.businessLogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamezap.R;

import java.util.List;

public class Adapter_GamePrice extends RecyclerView.Adapter<Adapter_GamePrice.MyViewHolder> {

    private List<GamePrice> gamePrices;
    private LayoutInflater mInflater;
    private Adapter_Game.ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_GamePrice(Context context, List<GamePrice> gamePrices) {
        this.mInflater = LayoutInflater.from(context);
        this.gamePrices = gamePrices;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public Adapter_GamePrice.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_companies, parent, false);
        return new Adapter_GamePrice.MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(Adapter_GamePrice.MyViewHolder holder, int position) {
        GamePrice gamePrice = gamePrices.get(position);
        holder.company_IMG_logo.setBackground(
                mInflater.getContext()
                        .getResources()
                        .getDrawable(gamePrice.getCompany().getLogo())
        );

        holder.company_IMG_logo.setOnClickListener(v -> {
            Intent browserIntent;
            if(gamePrices.get(position).getCompany().getName().equals("Steam")){
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://store.steampowered.com/app/" + gamePrices.get(position).getGame().getId()));
            }else {
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gamePrices.get(position).getCompany().getURL()));
            }
            mInflater.getContext().startActivity(browserIntent);
        });

        holder.company_TXT_name.setText(gamePrice.getCompany().getName());
        holder.company_TXT_price.setText("" + gamePrice.getPrice());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return gamePrices.size();
    }

    // convenience method for getting data at click position
    GamePrice getItem(int id) {
        return gamePrices.get(id);
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
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton company_IMG_logo;
        TextView company_TXT_name;
        TextView company_TXT_price;

        MyViewHolder(View itemView) {
            super(itemView);
            company_IMG_logo = itemView.findViewById(R.id.company_IMG_logo);
            company_TXT_name = itemView.findViewById(R.id.company_TXT_name);
            company_TXT_price = itemView.findViewById(R.id.company_TXT_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
