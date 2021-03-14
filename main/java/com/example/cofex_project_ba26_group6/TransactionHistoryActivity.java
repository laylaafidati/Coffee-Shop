package com.example.cofex_project_ba26_group6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cofex_project_ba26_group6.Adapter.TransactionListAdapter;
import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;

public class TransactionHistoryActivity extends AppCompatActivity {
    Button ivDeleteAll;
    RecyclerView transactionList;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);

        ivDeleteAll = findViewById(R.id.btn_DeleteAllTransactionHistory);
        transactionList = findViewById(R.id.rv_TransactionHistory);

        final TransactionListAdapter transactionListAdapter = new TransactionListAdapter(this, getIntent().getIntExtra("userId", 0));
        transactionList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        transactionList.setAdapter(transactionListAdapter);

        ivDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.dbDeleteAllTransaction(getIntent().getIntExtra("userId", 0));
                transactionListAdapter.dataChange();
                Toast.makeText(TransactionHistoryActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();
            }
        });
    }
}