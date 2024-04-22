package com.example.coffeeshopapp.model;

public class CartDetail {
    int idCart, idProduct, quantity,size;

    public CartDetail(int idCart, int idProduct, int quantity, int size) {
        this.idCart = idCart;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.size = size;
    }
    public CartDetail() {
    }

    public int getId() {
        return idCart;
    }

    public void setId(int idCart) {
        this.idCart = idCart;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
