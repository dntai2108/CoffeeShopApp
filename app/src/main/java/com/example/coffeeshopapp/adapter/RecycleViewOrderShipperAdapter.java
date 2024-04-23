package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public class RecycleViewOrderShipperAdapter extends RecyclerView.Adapter<RecycleViewOrderShipperAdapter.ViewHolder> {

    private final ArrayList<Order> orderList;
    private final Context context;

    public RecycleViewOrderShipperAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView idorder,ordertime,status,total;
        Button btnDuyet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idorder = itemView.findViewById(R.id.idorder);
            ordertime = itemView.findViewById(R.id.ordertime);
            status = itemView.findViewById(R.id.status);
            total = itemView.findViewById(R.id.total);
            btnDuyet = itemView.findViewById(R.id.btnDuyet);
        }
    }


    @NonNull
    @Override
    public RecycleViewOrderShipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_ordershipper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewOrderShipperAdapter.ViewHolder holder, int position) {
        Order o = orderList.get(position);
        holder.idorder.setText(o.getOrderId());
        holder.ordertime.setText(o.getOrderDate());
        holder.status.setText(o.getStatus());
        holder.total.setText(o.getTotalAmount());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
