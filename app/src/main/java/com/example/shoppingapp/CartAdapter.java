package com.example.shoppingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper dbHelper;
    private Shopping_cart shoppingCartActivity;
//
    // Constructor with Shopping_cart activity reference
    public CartAdapter(Context context, List<CartItem> cartItems, Shopping_cart shoppingCartActivity) {
        this.context = context;
        this.cartItems = cartItems;
        dbHelper = new DatabaseHelper(context);
        this.shoppingCartActivity = shoppingCartActivity;
    }

    @Override
    //on create view
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.productName.setText(item.getProductName());
        holder.productPrice.setText("$" + item.getUnitPrice());
        holder.productQuantity.setText("Qty: " + item.getQuantity());

        // Remove item from cart
        holder.removeButton.setOnClickListener(v -> {
            dbHelper.removeFromCart(item.getCartId());
            cartItems.remove(position);
            notifyItemRemoved(position);

            // Notify Shopping_cart to update the total
            shoppingCartActivity.updateTotal(cartItems);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productQuantity;
        Button removeButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
