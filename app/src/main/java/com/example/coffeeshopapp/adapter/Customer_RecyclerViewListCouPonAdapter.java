package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Coupon;

import java.util.ArrayList;

public class Customer_RecyclerViewListCouPonAdapter  extends RecyclerView.Adapter<Customer_RecyclerViewListCouPonAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Coupon> couponArrayList;

    public Customer_RecyclerViewListCouPonAdapter(Context context, ArrayList<Coupon> couponArrayList) {
        this.context = context;
        this.couponArrayList = couponArrayList;
    }

    @NonNull
    @Override
    public Customer_RecyclerViewListCouPonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.activity_customer_list_cou_pon,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Customer_RecyclerViewListCouPonAdapter.ViewHolder holder, int position) {
        Coupon coupon=couponArrayList.get(position);
        holder.tvCoupon.setText(coupon.getTenMa());
        holder.tvPhantramgiam.setText(coupon.getPhanTramGiam());
        holder.tvThoigianbatdau.setText(coupon.getNgayBatDau());
        holder.tvThoigianketthuc.setText(coupon.getNgayKetThuc());
    }

    @Override
    public int getItemCount() {
        return couponArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCoupon,tvThoigianbatdau,tvThoigianketthuc,tvPhantramgiam;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoupon=itemView.findViewById(R.id.tvCoupon);
            tvThoigianbatdau=itemView.findViewById(R.id.tvThoigianbatdau);
            tvThoigianketthuc=itemView.findViewById(R.id.tvThoigianketthuc);
            tvPhantramgiam=itemView.findViewById(R.id.tvPhantramgiam);
        }
    }
}
