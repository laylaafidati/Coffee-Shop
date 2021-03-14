package com.example.cofex_project_ba26_group6.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cofex_project_ba26_group6.CafeDetailActivity;
import com.example.cofex_project_ba26_group6.CafeListActivity;
import com.example.cofex_project_ba26_group6.Data.Cafe;
import com.example.cofex_project_ba26_group6.R;

import java.util.Vector;

public class CafeListAdapter extends RecyclerView.Adapter<CafeListAdapter.CustomHolder> {
    Vector<Cafe> vecCafe;
    int userId;

    public CafeListAdapter(Vector<Cafe> cafeList, int userId) {
        this.vecCafe = cafeList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public CafeListAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_cafe, parent, false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CafeListAdapter.CustomHolder holder, int position) {
        holder.cafeName.setText(vecCafe.get(position).getCafeName());
        holder.cafeRate.setText(String.valueOf(vecCafe.get(position).getRate()));
        holder.cafeAddress.setText(vecCafe.get(position).getAddress());
        Glide.with(holder.itemView).load(CafeListActivity.vecCafe.get(position).getImage()).into(holder.cafeImage);
    }

    @Override
    public int getItemCount() {
        return vecCafe.size();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        TextView cafeName;
        TextView cafeAddress;
        TextView cafeRate;
        ImageView cafeImage;

        public CustomHolder(@NonNull final View itemView) {
            super(itemView);
            cafeName = itemView.findViewById(R.id.tv_CafeName);
            cafeRate = itemView.findViewById(R.id.tv_CafeRate);
            cafeAddress = itemView.findViewById(R.id.tv_CafeAddress);
            cafeImage = itemView.findViewById(R.id.iv_CafeImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), CafeDetailActivity.class);
                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("userId", userId);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
