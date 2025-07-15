package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login_page extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private CheckBox rememberMeCheckBox;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_IS_ADMIN = "isAdmin";  // Added for admin check

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Load saved username and password if "Remember Me" was checked
        loadSavedCredentials();

        // Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login_page.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int userId = validateLogin(username, password);  // Get userId from validateLogin
                    if (userId != -1) {
                        // Grant admin permissions if username is "mohap"
                        boolean isAdmin = username.equalsIgnoreCase("mohap");

                        // Save credentials and admin status if "Remember Me" is checked
                        if (rememberMeCheckBox.isChecked()) {
                            saveCredentials(username, password, isAdmin, userId);
                        } else {
                            clearSavedCredentials();
                        }

                        // Navigate based on admin status
                        Intent intent;
                        if (isAdmin) {
                            intent = new Intent(Login_page.this, admin_page.class);  // Admin page for admin users
                        } else {
                            intent = new Intent(Login_page.this, MainActivity.class);  // Main page for regular users
                        }
                        intent.putExtra("USER_ID", userId);  // Pass the userId
                        startActivity(intent);
                        finish();  // Close the login activity

                    } else {
                        Toast.makeText(Login_page.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Register text click
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to register activity
                Intent intent = new Intent(Login_page.this, Register_page.class);
                startActivity(intent);
            }
        });

        // Forgot password click
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Password Recovery activity
                Intent intent = new Intent(Login_page.this, Password_recovery.class);
                startActivity(intent);
            }
        });
    }

    private void saveCredentials(String username, String password, boolean isAdmin, int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_REMEMBER, true);
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);  // Save admin status
        editor.putInt("user_id", userId);  // Save userId
        editor.apply();
    }

    private void clearSavedCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.putBoolean(KEY_REMEMBER, false);
        editor.remove(KEY_IS_ADMIN);  // Remove admin status
        editor.remove("user_id");
        editor.apply();
    }

    private void loadSavedCredentials() {
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            boolean isAdmin = sharedPreferences.getBoolean(KEY_IS_ADMIN, false);
            int userId = sharedPreferences.getInt("USER_ID", -1);  // Retrieve userId

            usernameEditText.setText(savedUsername);
            passwordEditText.setText(savedPassword);
            rememberMeCheckBox.setChecked(true);
        }
    }

    private int validateLogin(String username, String password) {
        // Check if the username and password are valid in the database
        int userId = databaseHelper.getUserId(username, password);  // Assuming you have a method to get userId
        return userId;  // Return userId, or -1 if not valid
    }
}
