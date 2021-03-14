package com.example.cofex_project_ba26_group6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cofex_project_ba26_group6.Data.User;
import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnSignIn, btnSignUp;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);

        if (dbHelper.getDatabaseData("User", "").getCount() == 0) {
            User user = new User("cofex", "master", "cofex@binus.ac.id", "+" + "62800000000", "unknown", "01-05-2020", "Cofex Street");
            dbHelper.dbInsert(user);
        }

        etUsername = findViewById(R.id.et_LoginUsername);
        etPassword = findViewById(R.id.et_LoginPassword);
        btnSignIn = findViewById(R.id.btn_Login);
        btnSignUp = findViewById(R.id.btn_Registration);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSignIn();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistration();
            }
        });
    }


    private void validateSignIn() {
        Cursor cursor = dbHelper.getDatabaseData("User", "");
        String inputUsername, inputPassword;
        inputUsername = etUsername.getText().toString();
        inputPassword = etPassword.getText().toString();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String username = cursor.getString(1);
            String password = cursor.getString(2);

            if (inputUsername.isEmpty()) {
                Toast.makeText(this, "Username must be filled", Toast.LENGTH_SHORT).show();
            } else if (inputPassword.isEmpty()) {
                Toast.makeText(this, "Password must be filled", Toast.LENGTH_SHORT).show();
            } else if (!inputUsername.equals(username) || !inputPassword.equals(password)) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            } else if (inputUsername.equals(username) && inputPassword.equals(password)) {
                Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, CafeListActivity.class);
//                    flag = 1;
                intent.putExtra("userId", id);
                intent.putExtra("currentPassword", password);
                startActivity(intent);
                finish();
            }
//            }
        }
    }

    private void openRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}