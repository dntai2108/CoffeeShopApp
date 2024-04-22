package com.example.coffeeshopapp.model;

public class Cart {
    private Productimgurl productimgurl;

    private String Quantity,Size;

    public Cart() {
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public Cart(Productimgurl productimgurl, String quantity, String size) {
        this.productimgurl = productimgurl;
        this.Size=size;
        this.Quantity = quantity;
    }




    public Productimgurl getProductimgurl() {
        return productimgurl;
    }

    public void setProductimgurl(Productimgurl productimgurl) {
        this.productimgurl = productimgurl;
    }



    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
