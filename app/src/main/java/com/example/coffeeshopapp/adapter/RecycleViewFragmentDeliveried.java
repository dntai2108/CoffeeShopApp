package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.shipper_chitietdonhang;
import com.example.coffeeshopapp.model.Customer;
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public class RecycleViewFragmentDeliveried extends RecyclerView.Adapter<RecycleViewFragmentDeliveried.ViewHolder>{
    private final ArrayList<Order> orderList;
    private final Context context;

    public RecycleViewFragmentDeliveried(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView idorder,ordertime,status,total;
        Button btnXem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idorder = itemView.findViewById(R.id.idorder);
            ordertime = itemView.findViewById(R.id.ordertime);
            status = itemView.findViewById(R.id.status);
            total = itemView.findViewById(R.id.total);
            btnXem = itemView.findViewById(R.id.btnXem);
        }
    }
    @NonNull
    @Override
    public RecycleViewFragmentDeliveried.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_delivery_shipper,parent,false);
        return new RecycleViewFragmentDeliveried.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewFragmentDeliveried.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.idorder.setText(order.getOrderId());
        holder.ordertime.setText(order.getOrderDate());
        holder.status.setText(order.getStatus());
        holder.total.setText(order.getStatus());
        holder.btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, shipper_chitietdonhang.class);
                String o = order.getOrderId();
                intent.putExtra("maDonHang", order.getOrderId());
                String tesst = order.getCustomerId();
                intent.putExtra("customer", order.getCustomerId());
                intent.putExtra("flag", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
