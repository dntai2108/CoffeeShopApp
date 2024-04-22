package com.example.coffeeshopapp.DAO;

import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public interface OrderDataListener {
    void onOrderListLoaded(ArrayList<Order> orderList);
    void onNumberList(ArrayList<Integer> yearList);
}
