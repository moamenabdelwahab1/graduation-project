package com.example.shoppingapp;

public class Transaction {
    private int id, userId;
    private String date, profit;

    public Transaction(int id, int userId, String date, String profit) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.profit = profit;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getProfit() {
        return profit;
    }
}

