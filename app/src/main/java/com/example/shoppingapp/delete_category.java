package com.example.shoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class delete_category extends AppCompatActivity {

    private Spinner categoryDeleteSpinner;
    private Button deleteCategoryButton;
    private DatabaseHelper databaseHelper;
    private ArrayList<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        // Initialize views
        categoryDeleteSpinner = findViewById(R.id.categoryDeleteSpinner);
        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Load categories into the spinner
        loadCategories();

        // Delete button click listener
        deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryToDelete = categoryDeleteSpinner.getSelectedItem().toString();

                if (categoryToDelete.isEmpty()) {
                    Toast.makeText(delete_category.this, "No category selected", Toast.LENGTH_SHORT).show();
                } else {
                    boolean deleted = databaseHelper.deleteCategory(categoryToDelete);
                    if (deleted) {
                        Toast.makeText(delete_category.this, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                        // Refresh the spinner
                        loadCategories();
                    } else {
                        Toast.makeText(delete_category.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
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
            categoryDeleteSpinner.setAdapter(adapter);
        }
    }
}
