package com.example.shoppingapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Edit_product extends AppCompatActivity {

    private EditText productNameEditText, priceEditText, quantityEditText;
    private Button loadProductButton, updateProductButton;
    private DatabaseHelper databaseHelper;
    private int productId = -1; // To hold the ID of the product being edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productNameEditText = findViewById(R.id.editProductName);
        priceEditText = findViewById(R.id.editProductPrice);
        quantityEditText = findViewById(R.id.editProductQuantity);
        loadProductButton = findViewById(R.id.loadProductButton);
        updateProductButton = findViewById(R.id.saveChangesButton);

        databaseHelper = new DatabaseHelper(this);

        loadProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameEditText.getText().toString().trim();
                if (productName.isEmpty()) {
                    Toast.makeText(Edit_product.this, "Please enter a product name", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadProductDetails(productName);
            }
        });

        updateProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productId == -1) {
                    Toast.makeText(Edit_product.this, "Please load a product first", Toast.LENGTH_SHORT).show();
                    return;
                }

                String priceText = priceEditText.getText().toString().trim();
                String quantityText = quantityEditText.getText().toString().trim();

                if (priceText.isEmpty() || quantityText.isEmpty()) {
                    Toast.makeText(Edit_product.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(quantityText);

                boolean isUpdated = databaseHelper.updateProduct(productId, price,  quantity);
                if (isUpdated) {
                    Toast.makeText(Edit_product.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Edit_product.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadProductDetails(String productName) {
        Cursor cursor = databaseHelper.getProductDetailsByName(productName);
        if (cursor.moveToFirst()) {
            productId = cursor.getInt(0);
            double price = cursor.getDouble(1);
            int quantity = cursor.getInt(2);

            priceEditText.setText(String.valueOf(price));
            quantityEditText.setText(String.valueOf(quantity));

            // Enable editing fields and update button
            priceEditText.setEnabled(true);
            quantityEditText.setEnabled(true);
            updateProductButton.setEnabled(true);

            Toast.makeText(this, "Product loaded successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
}
