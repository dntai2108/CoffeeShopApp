package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public class RecyclerViewDetaiOrderAdapter extends RecyclerView.Adapter<RecyclerViewDetaiOrderAdapter.ViewHolder>{
    private final ArrayList<Order> orderList;
    private final Context context;

    public RecyclerViewDetaiOrderAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMaDonHang_donhang,tvTime,tvTrangThai,tvTongTien;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDonHang_donhang=itemView.findViewById(R.id.tvMaDonHang_donhang);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvTrangThai=itemView.findViewById(R.id.tvTrangThai);
            tvTongTien=itemView.findViewById(R.id.tvTongTien);
        }
    }
}
