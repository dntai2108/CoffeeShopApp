package com.example.coffeeshopapp.adapter;

import static com.example.coffeeshopapp.activity.StatisticsActivity.selectedDisplay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.Chitiet_donhang_dadat_activity;
import com.example.coffeeshopapp.model.Order;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private ArrayList<Order> orderList;

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public OrderAdapter(Context context, ArrayList<Order> orderList) {
        super(context, 0, orderList);
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_order, parent, false);
        }

        // Lấy ra đối tượng Order tại vị trí position
        Order order = orderList.get(position);
        // Ánh xạ các view trong layout_item.xml
        TextView tvId = convertView.findViewById(R.id.idorder);
        TextView tvordertime = convertView.findViewById(R.id.ordertime);
        TextView tvstatus = convertView.findViewById(R.id.status);
        TextView tvtotal = convertView.findViewById(R.id.total);
        String orderId = order.getOrderId();

        // Hiển thị thông tin của Order lên các TextView tương ứng
        tvId.setText(order.getOrderId());
        tvordertime.setText(order.getOrderDate());
        tvstatus.setText(selectedDisplay);
        tvtotal.setText(order.getTotalAmount());
        return convertView;
    }
}
