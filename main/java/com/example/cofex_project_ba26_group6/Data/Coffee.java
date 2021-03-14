package com.example.cofex_project_ba26_group6.Data;

public class Coffee {
    int coffeeId;
    int cafeId;
    String coffeeName;
    String price;

    public Coffee(int coffeeId, int cafeId, String coffeeName, String price) {
        this.coffeeId = coffeeId;
        this.cafeId = cafeId;
        this.coffeeName = coffeeName;
        this.price = price;
    }

    public int getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(int coffeeId) {
        this.coffeeId = coffeeId;
    }

    public int getCafeId() {
        return cafeId;
    }

    public void setCafeId(int cafeId) {
        this.cafeId = cafeId;
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
