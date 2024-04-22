package com.example.coffeeshopapp.model;


import java.io.Serializable;


public class Product implements Serializable {

    private String image;

    private String id;
    private String name;

    private String price;

    private String description;
    private String size;
    private String date;


    public Product(String id, String name, String image, String price, String description, String size, String date) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.size = size;
        this.date = date;
    }

    public Product() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Product(String name, String image, String price) {
        this.name = name;
        this.image = image;
        this.price = price;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
