package com.example.cofex_project_ba26_group6;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cofex_project_ba26_group6.Adapter.CoffeeListAdapter;
import com.example.cofex_project_ba26_group6.Data.Cafe;
import com.example.cofex_project_ba26_group6.Data.Transaction;
import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CafeDetailActivity extends AppCompatActivity {
    TextView tvCafeName, tvCafeRate, tvCafeAddress;
    ImageView ivCafeImage;
    FloatingActionButton btnLocation;
    Button btnCheckout;
    RecyclerView coffeeList;
    CoffeeListAdapter coffeeAdapter;
    DatabaseHelper dbHelper;

    String cafeName, LAT, LNG, transactionDate;
    int userId;

    Dialog checkOutDialog;
    Button btnCheckoutYes, btnCheckoutNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_detail);

        dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
        checkOutDialog = new Dialog(this);

        tvCafeName = findViewById(R.id.tv_DetailCafeName);
        tvCafeRate = findViewById(R.id.tv_DetailCafeRate);
        tvCafeAddress = findViewById(R.id.tv_DetailCafeAddress);
        ivCafeImage = findViewById(R.id.iv_DetailCafeImage);
        btnLocation = findViewById(R.id.btn_DetailCafeLocation);
        btnCheckout = findViewById(R.id.btn_DetailCafeCheckout);
        coffeeList = findViewById(R.id.rv_DetailCafeCoffeeList);

        Cafe cafe = CafeListActivity.vecCafe.get(getIntent().getIntExtra("position", 0));
        userId = getIntent().getIntExtra("userId", 0);

        tvCafeName.setText(cafe.getCafeName());
        tvCafeAddress.setText(cafe.getAddress());
        tvCafeRate.setText(Integer.toString(cafe.getRate()));
        Glide.with(this).load(cafe.getImage()).into(ivCafeImage);
        LAT = cafe.getLAT();
        LNG = cafe.getLNG();
        cafeName = tvCafeName.getText().toString();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOut();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location();
            }
        });

        coffeeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        coffeeAdapter = new CoffeeListAdapter(this, getIntent().getIntExtra("position", 0) + 1);
        coffeeList.setAdapter(coffeeAdapter);
    }

    private void checkOut() {
        int counter = 0;
        String savedQuantity = "";
        int savedCoffeeId = 0;
        boolean isQuantityfilled = false;
        for (int i = 0; i < coffeeAdapter.getItemCount(); i++) {
            TextView quantity = coffeeList.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tv_Quantity);
            TextView coffeeName = coffeeList.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tv_CoffeeName);
            TextView coffeePrice = coffeeList.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tv_CoffeePrice);
            Log.d("tag", "checkOut: " + quantity.getText() + " index ke-" + i);
            if (!quantity.getText().toString().equals("0")) {
                counter += 1;
                isQuantityfilled = true;
                savedQuantity = quantity.getText().toString();
                Cursor coffee = dbHelper.getDatabaseData("Coffee", "coffeeName = '" + coffeeName.getText().toString() + "' and price = '" + coffeePrice.getText().toString() + "'");
                if (coffee.getCount() != 0) {
                    coffee.moveToFirst();
                    savedCoffeeId = coffee.getInt(0);
                }
            }
            if (!isQuantityfilled && i == coffeeAdapter.getItemCount() - 1) {
                Toast.makeText(CafeDetailActivity.this, "Please add some quantity!", Toast.LENGTH_SHORT).show();
            }
            if (counter > 1) {
                Toast.makeText(CafeDetailActivity.this, "Quantity must be filled and only 1 kind of coffee can be bought for each transaction!", Toast.LENGTH_SHORT).show();
                break;
            }
            if (isQuantityfilled && counter < 2 && i == coffeeAdapter.getItemCount() - 1) {
                openCheckOutDialog(savedQuantity, savedCoffeeId);
            }
        }
    }

    private void openCheckOutDialog(final String quantity, final int coffeeId) {
        checkOutDialog.setContentView(R.layout.alert_checkout_product);
        checkOutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btnCheckoutYes = checkOutDialog.findViewById(R.id.btn_CheckoutYes);
        btnCheckoutNo = checkOutDialog.findViewById(R.id.btn_CheckoutNo);

        checkOutDialog.show();
        btnCheckoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor userData = dbHelper.getDatabaseData("User", "ID = " + userId);
                Cursor coffeeData = dbHelper.getDatabaseData("Coffee", "ID = " + coffeeId);
                String sms = "";
                if (userData.moveToFirst() && coffeeData.moveToFirst()) {
                    sms = "You just purchasing from " + cafeName + ", " + coffeeData.getString(1) + " with quantity " + quantity + " with total price: " + Integer.parseInt(quantity) * coffeeData.getInt(2);
                }
                if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(userData.getString(4), null, sms, null, null);
                        Toast.makeText(CafeDetailActivity.this, "Success sending SMS", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CafeDetailActivity.this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                }
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                transactionDate = dateFormat.format(new Date(System.currentTimeMillis()));
                Transaction transaction = new Transaction(userId, coffeeId, quantity, transactionDate);
                boolean add = dbHelper.dbInsert(transaction);
                if (add == true) {
                    Toast.makeText(CafeDetailActivity.this, "Check out successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CafeDetailActivity.this, CafeListActivity.class);
                    intent.putExtra("userId", userId);
                    checkOutDialog.dismiss();
                    finishAffinity();
                    startActivity(intent);
                }
            }
        });
        btnCheckoutNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutDialog.dismiss();
            }
        });
    }

    private void location() {
        Intent intent = new Intent(CafeDetailActivity.this, MapActivity.class);
        intent.putExtra("cafeName", cafeName);
        intent.putExtra("LAT", LAT);
        intent.putExtra("LNG", LNG);
        startActivity(intent);
    }
}