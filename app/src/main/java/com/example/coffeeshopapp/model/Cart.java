package com.example.coffeeshopapp.model;

public class Cart {
    private Product productimgurl;
    private String Quantity;

    public Cart(Product productimgurl, String quantity) {
        this.productimgurl = productimgurl;
        Quantity = quantity;
    }

    public Cart() {
    }

    public Product getProductimgurl() {
        return productimgurl;
    }

    public void setProductimgurl(Product productimgurl) {
        this.productimgurl = productimgurl;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
