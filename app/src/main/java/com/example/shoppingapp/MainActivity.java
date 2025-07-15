package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView categoriesListView;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views and database helper
        categoriesListView = findViewById(R.id.categoriesListView);
        databaseHelper = new DatabaseHelper(this);

        // Assuming you have retrieved the userId from login or other activity
        int userId = getIntent().getIntExtra("USER_ID", -1);  // Retrieve userId passed from the previous activity

        // Load categories from the database
        loadCategories();

        // Handle item clicks on categories
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCategory = categories.get(position);

                // Pass both userId and selected category to ProductsActivity
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                intent.putExtra("USER_ID", userId);  // Pass the userId
                intent.putExtra("category", selectedCategory);  // Pass the selected category
                startActivity(intent);
            }
        });
    }

    private void loadCategories() {
        categories = databaseHelper.getAllCategories(); // Fetch all categories from the database

        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
        } else {
            // Set up the ListView with the categories
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
            categoriesListView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_cart) {
            // Retrieve the userId from the current intent (it was passed from the previous activity)
            int userId = getIntent().getIntExtra("USER_ID", -1);

            if (userId != -1) {
                // Pass the userId to the Shopping_cart activity
                Intent intent = new Intent(this, Shopping_cart.class);
                intent.putExtra("USER_ID", userId);  // Pass the userId
                startActivity(intent);
            } else {
                // Handle case where userId is not available
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

