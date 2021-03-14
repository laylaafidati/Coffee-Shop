package com.example.cofex_project_ba26_group6;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;
import com.example.cofex_project_ba26_group6.Function.Function;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText etOldPassword, etNewPassword, etConfirmNewPassword;
    Button btnUpdatePassword;
    DatabaseHelper dbHelper;
    int userId;
    String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);
        final Cursor userData = dbHelper.getDatabaseData("User", "ID = " + getIntent().getIntExtra("userId", 0));
        etOldPassword = findViewById(R.id.et_CurrentPassword);
        etNewPassword = findViewById(R.id.et_NewPassword);
        etConfirmNewPassword = findViewById(R.id.et_ConfirmNewPassword);
        btnUpdatePassword = findViewById(R.id.btn_UpdatePassword);

        userId = getIntent().getIntExtra("userId", 0);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUpdatePassword(userData);
            }
        });
    }

    private void validateUpdatePassword(Cursor userData) {
        if (userData.moveToFirst()) {
            String oldPassword, newPassword, confirmNewPassword;

            oldPassword = etOldPassword.getText().toString();
            newPassword = etNewPassword.getText().toString();
            confirmNewPassword = etConfirmNewPassword.getText().toString();

            if (!oldPassword.equals(userData.getString(2))) {
                Toast.makeText(this, "Old password must be the same with current password", Toast.LENGTH_LONG).show();
            } else if (oldPassword.equals(userData.getString(2))) {
                if (newPassword.length() < 5) {
                    Toast.makeText(this, "New password must be at least 5 characters", Toast.LENGTH_LONG).show();
                } else if (!new Function().isAlphaNumeric(newPassword)) {
                    Toast.makeText(this, "New Password must be alphanumeric ", Toast.LENGTH_SHORT).show();
                } else if (!confirmNewPassword.equals(newPassword)) {
                    Toast.makeText(this, "Confirm password and new password must be the same", Toast.LENGTH_LONG).show();
                } else {
                    dbHelper.dbUpdateUserPassword(userId, newPassword);
                    Toast.makeText(this, "New password is updated!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, CafeListActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }
    }
}