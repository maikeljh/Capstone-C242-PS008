package com.example.culinairy.model;

public class Product {
    private final String name;
    private final int quantity;
    private final double price;
    private final double totalPrice;

    public Product(String name, int quantity, double price, double totalPrice) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}

