package com.example.coffeeshopapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order implements Serializable {
    private String orderId;
    private List<Cart> cartList;
    private String status;
    private String orderDate;
    private String totalAmount;
    private Customer customer;

    public Order(String orderId, List<Cart> cartList, String status, String orderDate, String totalAmount, Customer customer) {
        this.orderId = orderId;
        this.cartList = cartList;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
    }

    public Order(String madonhang, String time, String status, String tongtien) {
        this.orderId = madonhang;
        this.orderDate = time;
        this.status = status;
        this.totalAmount = tongtien;
    }

    public Order(String time, String status, String tongtien) {
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order() {
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


}