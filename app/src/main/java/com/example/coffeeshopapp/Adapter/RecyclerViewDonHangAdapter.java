package com.example.coffeeshopapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.Chitiet_donhang_dadat_activity;
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public class RecyclerViewDonHangAdapter extends RecyclerView.Adapter<RecyclerViewDonHangAdapter.ViewHolder> {
    private final ArrayList<Order> orderList;
    private final Context context;


    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RecyclerViewDonHangAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDonHang_donhang, tvTime, tvTrangThai, tvTongTien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang_donhang = itemView.findViewById(R.id.tvMaDonHang_donhang);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
        }
    }

    @NonNull
    @Override
    public RecyclerViewDonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_donhang, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDonHangAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvMaDonHang_donhang.setText(order.getOrderId());
        holder.tvTime.setText(order.getOrderDate());
        holder.tvTongTien.setText(order.getTotalAmount());
        holder.tvTrangThai.setText(order.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chitiet_donhang_dadat_activity.class);
                intent.putExtra("madonhang", order.getOrderId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}