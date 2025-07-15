package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Password_recovery extends AppCompatActivity {

    private EditText inputField, newPasswordField;
    private Button submitPasswordButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        inputField = findViewById(R.id.inputField);
        newPasswordField = findViewById(R.id.newPasswordField);
        submitPasswordButton = findViewById(R.id.submitPasswordButton);
        databaseHelper = new DatabaseHelper(this);

        submitPasswordButton.setOnClickListener(v -> {
            String input = inputField.getText().toString().trim();
            String newPassword = newPasswordField.getText().toString().trim();

            if (input.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = databaseHelper.resetPassword(input, newPassword);

            if (success) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            } else {
                Toast.makeText(this, "User not found or password update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

