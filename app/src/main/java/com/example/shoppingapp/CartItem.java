package com.example.shoppingapp;

public class CartItem {
    private int cartId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public CartItem(int cartId, String productName, double unitPrice, int quantity) {
        this.cartId = cartId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Getters 
    public int getCartId() {
        return cartId;
    }
//seters
    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
