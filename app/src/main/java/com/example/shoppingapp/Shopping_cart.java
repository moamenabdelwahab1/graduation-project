package com.example.shoppingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Shopping_cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private DatabaseHelper dbHelper;
    private Button purchaseButton;
    private TextView totalTextView;
    private List<CartItem> cartItems;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Retrieve userId passed from MainActivity
        userId = getIntent().getIntExtra("USER_ID", 1);

        recyclerView = findViewById(R.id.recyclerViewCart);
        totalTextView = findViewById(R.id.totalTextView);
        purchaseButton = findViewById(R.id.purchaseButton);
        dbHelper = new DatabaseHelper(this);

        // Fetch cart items from database for the user
        cartItems = dbHelper.getCartItems(userId);

        // Set up RecyclerView
        cartAdapter = new CartAdapter(this, cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        // Calculate total
        double total = calculateTotal(cartItems);
        totalTextView.setText("Total: $" + total);

        // Handle Purchase Button click
        purchaseButton.setOnClickListener(v -> {
            // Modify date format to only include year, month, and day
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            boolean transactionAdded = dbHelper.addTransaction(userId, currentDate, total);

            if (transactionAdded) {
                // Clear the cart after purchase
                dbHelper.clearCart(userId);

                // Show success message
                Toast.makeText(Shopping_cart.this, "Purchase Successful!", Toast.LENGTH_SHORT).show();

                // Clear the cart items from RecyclerView
                cartItems.clear();
                cartAdapter.notifyDataSetChanged();

                // Update the total and cart display
                totalTextView.setText("Total: $0.00");
            } else {
                Toast.makeText(Shopping_cart.this, "Failed to add transaction.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getUnitPrice() * item.getQuantity();
        }
        return total;
    }
//update commet
    public void updateTotal(List<CartItem> cartItems) {
        double total = calculateTotal(cartItems);
        totalTextView.setText("Total: $" + total);
    }
}
