package com.example.coffeeshopapp.model;

public class DataClass {
    private String imgUrl;

    public DataClass(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public DataClass(){}

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
