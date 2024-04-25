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
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;

public class RecyclerViewStatiticAdapter extends RecyclerView.Adapter<RecyclerViewStatiticAdapter.ViewHolder> {
    private final ArrayList<Order> orderList;

    private final Context context;

    public RecyclerViewStatiticAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView idOrder, tvPrice,tvStatus,orderTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            idOrder = itemView.findViewById(R.id.idorder);
            tvPrice = itemView.findViewById(R.id.total);
            tvStatus = itemView.findViewById(R.id.status);
            orderTime = itemView.findViewById(R.id.ordertime);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.layout_item_order,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.idOrder.setText(order.getOrderId());
        holder.orderTime.setText(order.getOrderDate());
        holder.tvStatus.setText(order.getStatus());
        holder.tvPrice.setText(order.getTotalAmount());
       /* holder.tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Chitiet_donhang_dadat_activity.class);
                intent.putExtra("madonhang",order.getOrderId());
                context.startActivity(intent);
            }
        });*/
    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
