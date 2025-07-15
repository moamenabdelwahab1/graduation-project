package com.example.shoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class edit_category extends AppCompatActivity {

    private Spinner categorySpinner;
    private EditText newCategoryEditText;
    private Button updateCategoryButton;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        // Initialize views
        categorySpinner = findViewById(R.id.categorySpinner);
        newCategoryEditText = findViewById(R.id.newCategoryEditText);
        updateCategoryButton = findViewById(R.id.updateCategoryButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Load categories into the spinner
        loadCategories();

        // Update button click listener
        updateCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldCategory = categorySpinner.getSelectedItem().toString();
                String newCategory = newCategoryEditText.getText().toString().trim();

                if (newCategory.isEmpty()) {
                    Toast.makeText(edit_category.this, "Please enter a new category name", Toast.LENGTH_SHORT).show();
                } else {
                    boolean updated = databaseHelper.updateCategory(oldCategory, newCategory);
                    if (updated) {
                        Toast.makeText(edit_category.this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                        // Refresh the spinner
                        loadCategories();
                    } else {
                        Toast.makeText(edit_category.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadCategories() {
        categories = databaseHelper.getAllCategories();

        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        }
    }
}
