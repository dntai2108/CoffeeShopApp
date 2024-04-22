package com.example.coffeeshopapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Productimgurl implements Parcelable {
    private String id,name,imgurl,price,size;

    public Productimgurl(String id,String name, String imgurl, String price, String size) {
        this.name = name;
        this.imgurl = imgurl;
        this.price = price;
        this.size=size;
        this.id=id;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Productimgurl() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imgurl);
        dest.writeString(price);
        dest.writeString(size);
    }
    protected Productimgurl(Parcel in) {
        id = in.readString();
        name = in.readString();
        imgurl = in.readString();
        price = in.readString();
        size = in.readString();
    }

    public static final Creator<Productimgurl> CREATOR = new Creator<Productimgurl>() {
        @Override
        public Productimgurl createFromParcel(Parcel in) {
            return new Productimgurl(in);
        }

        @Override
        public Productimgurl[] newArray(int size) {
            return new Productimgurl[size];
        }
    };
}
