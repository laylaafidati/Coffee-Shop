package com.example.cofex_project_ba26_group6.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;
import com.example.cofex_project_ba26_group6.R;

public class CoffeeListAdapter extends RecyclerView.Adapter<CoffeeListAdapter.CustomHolder> {
    Cursor coffeeCursor;
    DatabaseHelper dbHelper;
    public CoffeeListAdapter(Context context, int cafeId) {
        dbHelper = new DatabaseHelper(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
        this.coffeeCursor = dbHelper.getDatabaseData("Coffee", " cafeId = " + cafeId);
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_coffee, parent, false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomHolder holder, final int position) {
        if (coffeeCursor.moveToPosition(position)) {
            holder.itemView.setTag(coffeeCursor.getInt(0));
            holder.coffeeName.setText(coffeeCursor.getString(1));
            holder.prices.setText(coffeeCursor.getString(2));
            int imageId = holder.images.getResources().getIdentifier(coffeeCursor.getString(1).trim().replaceAll(" ", "").toLowerCase(), "drawable", holder.images.getContext().getPackageName());
            holder.images.setImageResource(imageId);
            Log.d("adapterkopi", "coffeeID: " + coffeeCursor.getInt(0));
        }

    }

    @Override
    public int getItemCount() {
        return coffeeCursor.getCount();
    }

    public class CustomHolder extends RecyclerView.ViewHolder {
        TextView coffeeName;
        TextView prices;
        Button btDecreaase, btIncrease;
        ImageView images;
        TextView tvQuantity;

        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            coffeeName = itemView.findViewById(R.id.tv_CoffeeName);
            prices = itemView.findViewById(R.id.tv_CoffeePrice);
            btDecreaase = itemView.findViewById(R.id.btn_DecreaseQuantity);
            btIncrease = itemView.findViewById(R.id.btn_IncreaseQuantity);
            images = itemView.findViewById(R.id.iv_CoffeeImage);
            tvQuantity = itemView.findViewById(R.id.tv_Quantity);

            btIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvQuantity.setText(String.valueOf(Integer.parseInt(tvQuantity.getText().toString()) + 1));
                }
            });
            btDecreaase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!tvQuantity.getText().toString().equals("0")) {
                        tvQuantity.setText(String.valueOf(Integer.parseInt(tvQuantity.getText().toString()) - 1));
                    }
                }
            });
        }
    }
}