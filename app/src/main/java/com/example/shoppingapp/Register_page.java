package com.example.shoppingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Register_page extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private TextView birthdateTextView;
    private Button registerButton;
    private DatabaseHelper databaseHelper;
    private String selectedBirthdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        birthdateTextView = findViewById(R.id.birthdateTextView);
        registerButton = findViewById(R.id.registerButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Select birthdate
        birthdateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Register button click
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedBirthdate.isEmpty()) {
                    Toast.makeText(Register_page.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(Register_page.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkUsernameExists(username)) {
                    Toast.makeText(Register_page.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (databaseHelper.checkEmailExists(email)) {
                    Toast.makeText(Register_page.this, "Email already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean isInserted = databaseHelper.addUser(username, email, password, selectedBirthdate, 0);
                if (isInserted) {
                    Toast.makeText(Register_page.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register_page.this, Login_page.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Register_page.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Register_page.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedBirthdate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    birthdateTextView.setText(selectedBirthdate);
                }, year, month, day);
        datePickerDialog.show();
    }
}
