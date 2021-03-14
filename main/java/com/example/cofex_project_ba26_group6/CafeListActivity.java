package com.example.cofex_project_ba26_group6;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cofex_project_ba26_group6.Adapter.CafeListAdapter;
import com.example.cofex_project_ba26_group6.Data.Cafe;
import com.example.cofex_project_ba26_group6.Data.Coffee;
import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class CafeListActivity extends AppCompatActivity {
    public static Vector<Cafe> vecCafe = new Vector<>();
    final String url = "https://raw.githubusercontent.com/chandratan03/MCS_Project_Images/master/api.json";
    RecyclerView cafeList;
    int userId;
    String currentPassword;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_list);
        dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);

        cafeList = findViewById(R.id.rv_CafeList);

        userId = getIntent().getIntExtra("userId", 0);
        currentPassword = getIntent().getStringExtra("currentPassword");
        getCafeListData();
        cafeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cafeList.setAdapter(new CafeListAdapter(vecCafe, userId));
        addCoffee();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menubar = getMenuInflater();
        menubar.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.id_transactionHistory:
                intent = new Intent(this, TransactionHistoryActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;
            case R.id.id_changePassword:
                intent = new Intent(this, ChangePasswordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;
            case R.id.id_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(this, "You Sucessfully Logged Out!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getCafeListData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        if (!vecCafe.isEmpty()) {
                            for (int j = 0; j < vecCafe.size(); j++) {
                                if (jsonObject.getInt("id") == vecCafe.get(j).getId()) {
                                    break;
                                } else if (j == vecCafe.size() - 1) {
                                    vecCafe.add(new Cafe(jsonObject.getInt("id")
                                            , jsonObject.getString("cafeName")
                                            , jsonObject.getString("address")
                                            , jsonObject.getString("LAT")
                                            , jsonObject.getString("LNG")
                                            , jsonObject.getString("image")
                                            , jsonObject.getInt("rate")));
                                }
                            }
                        } else {
                            vecCafe.add(new Cafe(jsonObject.getInt("id")
                                    , jsonObject.getString("cafeName")
                                    , jsonObject.getString("address")
                                    , jsonObject.getString("LAT")
                                    , jsonObject.getString("LNG")
                                    , jsonObject.getString("image")
                                    , jsonObject.getInt("rate")));
                        }
                        cafeList.getAdapter().notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(CafeListActivity.this, "Failed to get Cafe List data, please re open to retry!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void addCoffee() {
        dbHelper.dbInsert(new Coffee(0, 1, "Latte", "20000"));
        dbHelper.dbInsert(new Coffee(0, 1, "Americano", "15000"));
        dbHelper.dbInsert(new Coffee(0, 1, "Cappuccino", "25000"));
        dbHelper.dbInsert(new Coffee(0, 2, "Macchiato", "16000"));
        dbHelper.dbInsert(new Coffee(0, 2, "Espresso", "18000"));
        dbHelper.dbInsert(new Coffee(0, 3, "Long Black", "16000"));
        dbHelper.dbInsert(new Coffee(0, 3, "Irish Coffee", "18000"));
        dbHelper.dbInsert(new Coffee(0, 3, "Flat White", "18000"));
        dbHelper.dbInsert(new Coffee(0, 4, "Vienna", "16000"));
        dbHelper.dbInsert(new Coffee(0, 4, "Affogato", "18000"));
        dbHelper.dbInsert(new Coffee(0, 5, "Dalgona", "16000"));
        dbHelper.dbInsert(new Coffee(0, 5, "Filter Coffee", "18000"));
    }
}