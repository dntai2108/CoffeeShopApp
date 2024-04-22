package com.example.coffeeshopapp.model;

import java.util.Timer;

public class Order {
    int id, idAddress;
    int  idcoupon,idemlpyee;
    String orderTime;
    double total;
    float shipperFee;
    int status;
    String paymethod;
     Order()
     {}

    public Order(int id, int status) {
        this.id = id;
        this.status = status;
    }

    public Order(int id, String orderTime, double total, int status) {
        this.id = id;
        this.orderTime = orderTime;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    public int getIdcoupon() {
        return idcoupon;
    }

    public void setIdcoupon(int idcoupon) {
        this.idcoupon = idcoupon;
    }

    public int getIdemlpyee() {
        return idemlpyee;
    }

    public void setIdemlpyee(int idemlpyee) {
        this.idemlpyee = idemlpyee;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public float getShipperFee() {
        return shipperFee;
    }

    public void setShipperFee(float shipperFee) {
        this.shipperFee = shipperFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }
}
