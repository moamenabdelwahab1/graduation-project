package com.example.shoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class Delete_product extends AppCompatActivity {

    private EditText editProductName;
    private Spinner spinnerCategory;
    private Button deleteProductButton;
    private DatabaseHelper dbHelper;
    private ArrayList<String> categoryNames;
    private HashMap<String, Integer> categoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        editProductName = findViewById(R.id.editProductName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        deleteProductButton = findViewById(R.id.deleteProductButton);

        // Load categories into spinner
        loadCategories();

        // Set delete button listener
        deleteProductButton.setOnClickListener(v -> deleteProduct());
    }

    private void loadCategories() {
        // Fetch all categories from the database and add them to the spinner
        categoryNames = new ArrayList<>();
        categoryMap = dbHelper.getCategoryMap();  // Fetch category names and IDs map

        // Add category names to the list
        categoryNames.addAll(categoryMap.keySet());

        // Create an ArrayAdapter to bind the category names to the Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Set a listener for category selection (optional, if you need it)
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Optionally handle category selection changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Optionally handle the case where no category is selected
            }
        });
    }

    private void deleteProduct() {
        String productName = editProductName.getText().toString().trim();
        String categoryName = spinnerCategory.getSelectedItem().toString();
        int categoryId = categoryMap.get(categoryName);

        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query database to check if the product exists in the given category
        boolean isProductDeleted = dbHelper.deleteProductByNameAndCategory(productName, categoryId);

        // Provide feedback to the user
        if (isProductDeleted) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product not found in the selected category", Toast.LENGTH_SHORT).show();
        }
    }
}
