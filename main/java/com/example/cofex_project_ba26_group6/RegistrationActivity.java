package com.example.cofex_project_ba26_group6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cofex_project_ba26_group6.Data.User;
import com.example.cofex_project_ba26_group6.Database.DatabaseHelper;
import com.example.cofex_project_ba26_group6.Function.Function;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity {
    DatePicker dpDOB;
    RadioButton rbMale, rbFemale;
    CheckBox cbAgreement;
    Button btnRegister, btnLogin;
    EditText etUsername, etPassword, etConfirmPassword, etPhoneNumber, etEmail, etAddress;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION);

        etUsername = findViewById(R.id.et_RegistrationUsername);
        etPassword = findViewById(R.id.et_RegistrationPassword);
        etConfirmPassword = findViewById(R.id.et_RegistrationConfirmPassword);
        etPhoneNumber = findViewById(R.id.et_RegistrationPhone);
        etEmail = findViewById(R.id.et_RegistrationEmail);
        etAddress = findViewById(R.id.et_RegistrationAddress);
        dpDOB = findViewById(R.id.dp_RegistrationDOB);
        final Date date = new Date();
        dpDOB.setMaxDate(date.getTime());
        rbMale = findViewById(R.id.rb_RegistrationMale);
        rbFemale = findViewById(R.id.rb_RegistrationFemale);
        cbAgreement = findViewById(R.id.cb_RegistrationAgreement);
        btnRegister = findViewById(R.id.btn_Registration);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegistration();
            }
        });

    }

    private void validateRegistration() {
        String username, password, confirmPassword, email, phoneNumber, gender, address, agreement;
        int dateDOB, monthDOB, yearDOB;

        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();
        email = etEmail.getText().toString();
        phoneNumber = etPhoneNumber.getText().toString();
        dateDOB = dpDOB.getDayOfMonth();
        monthDOB = dpDOB.getMonth() + 1;
        yearDOB = dpDOB.getYear();
        address = etAddress.getText().toString();

        if (username.length() < 5 || username.length() > 25) {
            Toast.makeText(this, "Username must be between 5-25 characters", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(this, "Password must be at least 5 characters", Toast.LENGTH_SHORT).show();
        } else if (!new Function().isAlphaNumeric(password)) {
            Toast.makeText(this, "Password must be alphanumeric ", Toast.LENGTH_SHORT).show();
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Confirm password must be equals to password", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.length() < 8 || phoneNumber.length() > 14) {
            Toast.makeText(this, "Phone number must be between 8-14 characters", Toast.LENGTH_SHORT).show();
        } else if (!phoneNumber.startsWith("62")) {
            Toast.makeText(this, "Phone number must starts with ‘62’", Toast.LENGTH_SHORT).show();
        } else if (!rbMale.isChecked() && !rbFemale.isChecked()) {
            Toast.makeText(this, "Gender must be selected", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("@") || !email.endsWith(".com")) {
            Toast.makeText(this, "Email must contains only one ‘@’ and must ends with '.com'", Toast.LENGTH_SHORT).show();
        } else if (!address.endsWith(" street")) {
            Toast.makeText(this, "Address must ends with ‘street’", Toast.LENGTH_SHORT).show();
        } else if (!isDateValid(dateDOB, monthDOB, yearDOB)) {
            Toast.makeText(this, "You must be at least 18 years old!", Toast.LENGTH_SHORT).show();
        } else if (!cbAgreement.isChecked()) {
            Toast.makeText(this, "Agreement must be checked", Toast.LENGTH_SHORT).show();
        } else {
            if (rbMale.isChecked()) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            User user = new User(username, password, email, "+" + phoneNumber, gender, dateDOB + "-" + monthDOB + "-" + yearDOB, address);
            boolean add = dbHelper.dbInsert(user);
            if (add == true) {
                Toast.makeText(this, "Registration is success!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    protected boolean isDateValid(int dateDOB, int monthDOB, int yearDOB) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date inputDate = new Date(System.currentTimeMillis());
        String currentDate = dateFormat.format(inputDate);
        int currentDay = Integer.parseInt(currentDate.substring(0, 2));
        int currentMonth = Integer.parseInt(currentDate.substring(3, 5));
        int currentYear = Integer.parseInt(currentDate.substring(6));
        if (currentYear - yearDOB < 18) {
            Toast.makeText(RegistrationActivity.this, "You must be older than 18 years old!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (currentYear - yearDOB == 18) {
            if (currentMonth < monthDOB) {
                Toast.makeText(RegistrationActivity.this, "You must be older than 18 years old!", Toast.LENGTH_SHORT).show();
                return false;
            } else if (currentMonth == monthDOB) {
                if (currentDay < dateDOB) {
                    Toast.makeText(RegistrationActivity.this, "You must be older than 18 years old!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }
}