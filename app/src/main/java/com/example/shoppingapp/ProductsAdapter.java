package com.example.shoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<Product> productList;
    private List<Product> filteredProductList;
    private DatabaseHelper dbHelper;
    private int userId;

    public ProductsAdapter(List<Product> productList, DatabaseHelper dbHelper, int userId) {
        this.productList = productList;
        this.filteredProductList = new ArrayList<>(productList); // Initialize filtered list
        this.dbHelper = dbHelper;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = filteredProductList.get(position);

        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(String.format("$%.2f", product.getPrice()));
        holder.quantityEditText.setText("1");

        holder.addToCartButton.setOnClickListener(v -> {
            int quantity;
            try {
                quantity = Integer.parseInt(holder.quantityEditText.getText().toString());
                if (quantity <= 0) {
                    Toast.makeText(holder.itemView.getContext(), "Quantity must be at least 1", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(holder.itemView.getContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add product to cart
            boolean success = dbHelper.addToCart(userId, product.getId(), product.getPrice(), quantity);
            if (success) {
                Toast.makeText(holder.itemView.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size(); // Use filtered list instead of original productList
    }

    // Filter products based on search query
    public void filter(String query) {
        if (query.isEmpty()) {
            filteredProductList = new ArrayList<>(productList); // Reset to original list
        } else {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            filteredProductList = filteredList;
        }
        notifyDataSetChanged(); // Update the RecyclerView
    }



    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView, productPriceTextView;
        EditText quantityEditText;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            productPriceTextView = itemView.findViewById(R.id.productPriceTextView);
            quantityEditText = itemView.findViewById(R.id.quantityEditText);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
