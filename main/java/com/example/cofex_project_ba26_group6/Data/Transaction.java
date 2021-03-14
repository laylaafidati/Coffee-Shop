package com.example.cofex_project_ba26_group6.Data;

public class Transaction {
    int userId;
    int coffeeId;
    String quantity;
    String date;

    public Transaction(int userId, int coffeeId, String quantity, String date) {
        this.userId = userId;
        this.coffeeId = coffeeId;
        this.quantity = quantity;
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(int coffeeId) {
        this.coffeeId = coffeeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
