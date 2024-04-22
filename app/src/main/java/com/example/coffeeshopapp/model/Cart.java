package com.example.coffeeshopapp.model;

public class Cart {
    private Product product;
    private String Quantity;

    private String size;

    public Cart(Product product, String quantity, String size) {
        this.product = product;
        this.Quantity = quantity;
        this.size = size;
    }

    public Cart() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
