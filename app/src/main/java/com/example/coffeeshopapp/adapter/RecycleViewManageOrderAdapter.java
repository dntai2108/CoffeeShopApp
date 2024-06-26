package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.Chitiet_donhang_dadat_activity;
import com.example.coffeeshopapp.activity.Chitiet_donhang_dadat_activity_admin;
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public class RecycleViewManageOrderAdapter extends RecyclerView.Adapter<RecycleViewManageOrderAdapter.ViewHolder> {
    private final ArrayList<Order> orderList;

    private final Context context;

    public RecycleViewManageOrderAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
       ImageView imgOrder,imgState;
       TextView tvIdOrder, tvNgayDat,tvStatus,tvOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.imgOrder);
            imgState = itemView.findViewById(R.id.imgState);
            tvIdOrder = itemView.findViewById(R.id.tvIdOrder);
            tvNgayDat = itemView.findViewById(R.id.tvNgayDat);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrder = itemView.findViewById(R.id.tvOrder);
        }
    }

    @NonNull
    @Override
    public RecycleViewManageOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custome_recycleview_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewManageOrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvIdOrder.setText(order.getOrderId());
        holder.tvNgayDat.setText(order.getOrderDate());
        holder.tvStatus.setText(order.getStatus());
        holder.tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chitiet_donhang_dadat_activity_admin.class);
                intent.putExtra("madonhang",order.getOrderId());
                intent.putExtra("customerId", order.getCustomerId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
