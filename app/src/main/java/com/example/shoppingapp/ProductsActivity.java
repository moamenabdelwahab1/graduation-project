package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private DatabaseHelper dbHelper;
    private int userId;  // Declare a variable to hold userId
    private ProductsAdapter adapter;
    private List<Product> productList;
    private EditText searchEditText;
    private Button voiceSearchButton, barcodeSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the RecyclerView and DatabaseHelper
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        dbHelper = new DatabaseHelper(this);

        // Get the category name and userId passed from the previous activity
        String categoryName = getIntent().getStringExtra("category");
        userId = getIntent().getIntExtra("USER_ID", -1);  // Retrieve userId from Intent

        if (categoryName == null || categoryName.isEmpty()) {
            Toast.makeText(this, "Invalid Category", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Resolve category name to ID
        HashMap<String, Integer> categoryMap = dbHelper.getCategoryMap();
        int categoryId = -1;
        if (categoryMap != null && categoryMap.containsKey(categoryName)) {
            categoryId = categoryMap.get(categoryName);
        }

        if (categoryId == -1) {
            Toast.makeText(this, "Category not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("ProductsActivity", "Selected Category Name: " + categoryName);
        Log.d("ProductsActivity", "Resolved Category ID: " + categoryId);
        Log.d("ProductsActivity", "User ID: " + userId);  // Log the userId to verify it's being passed correctly

        // Fetch products for the resolved category ID
        productList = dbHelper.getProductsByCategory(categoryId);

        if (productList.isEmpty()) {
            Toast.makeText(this, "No products found in this category", Toast.LENGTH_SHORT).show();
        }

        // Set up the RecyclerView with the product list
        adapter = new ProductsAdapter(productList, dbHelper, userId);  // Pass userId to the adapter if needed
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productsRecyclerView.setAdapter(adapter);

        // Set up search views
        searchEditText = findViewById(R.id.searchEditText);
        voiceSearchButton = findViewById(R.id.voiceSearchButton);
        barcodeSearchButton = findViewById(R.id.barcodeSearchButton);

        // Set listeners for search functionality
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString();
                adapter.filter(query);  // Filter products based on text search
            }

            @Override
            public void afterTextChanged(android.text.Editable editable) { }
        });

        voiceSearchButton.setOnClickListener(v -> startVoiceSearch());

        barcodeSearchButton.setOnClickListener(v -> startBarcodeScan());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int userId = getIntent().getIntExtra("USER_ID", -1);
        // Handle item selection
        if (item.getItemId() == R.id.action_cart) {
            // Navigate to Shopping_cart activity
            Intent intent = new Intent(this, Shopping_cart.class);
            intent.putExtra("USER_ID", userId);  // Pass the userId
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Start voice search using Android's Speech Recognition
    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, 100);
    }

    // Handle voice search results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            List<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String query = matches.get(0);  // Use the first result from the voice recognition
                searchEditText.setText(query);  // Optionally display the voice input in the EditText
                adapter.filter(query);  // Filter products based on the voice query
            }
        }
    }

   
    private void startBarcodeScan() {
        // Example: Start a barcode scanning activity (e.g., using ZXing or another library)
        // Intent intent = new Intent(this, BarcodeScannerActivity.class);
        // startActivityForResult(intent, 200);

        // For now, display a dummy result (you can replace this with actual scanning code)
        String dummyBarcode = "1234567890";  // Example barcode
         // Filter products by barcode
    }
}
