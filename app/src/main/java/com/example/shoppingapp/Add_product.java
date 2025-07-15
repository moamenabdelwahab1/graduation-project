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
import java.util.HashMap;
import java.util.List;

public class Add_product extends AppCompatActivity {

    private EditText productNameEditText, priceEditText, quantityEditText;
    private Spinner categorySpinner;
    private Button addProductButton;
    private DatabaseHelper databaseHelper;
    private HashMap<String, Integer> categoryMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productNameEditText = findViewById(R.id.productNameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        addProductButton = findViewById(R.id.addProductButton);

        databaseHelper = new DatabaseHelper(this);

        loadCategories();

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameEditText.getText().toString().trim();
                String priceText = priceEditText.getText().toString().trim();
                String quantityText = quantityEditText.getText().toString().trim();
                String selectedCategory = categorySpinner.getSelectedItem().toString();

                if (productName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || selectedCategory.isEmpty()) {
                    Toast.makeText(Add_product.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);
                int categoryId = categoryMap.get(selectedCategory);

                boolean isAdded = databaseHelper.addProduct(productName, categoryId, price, quantity);
                if (isAdded) {
                    Toast.makeText(Add_product.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Add_product.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCategories() {
        categoryMap = databaseHelper.getCategoryMap();
        List<String> categoryNames = new ArrayList<>(categoryMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }
}
