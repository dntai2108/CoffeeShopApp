package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Product;

import java.util.ArrayList;

public class FlashSaleApdapter extends RecyclerView.Adapter<FlashSaleApdapter.ViewHolder> {
    private ArrayList<Product> flashSaleList;
    private Context context;

    public FlashSaleApdapter() {

    }

    public void setFlashSaleList(ArrayList<Product> flashSaleList) {
        this.flashSaleList = flashSaleList;
    }

    public FlashSaleApdapter(ArrayList<Product> products, Context context) {
        this.flashSaleList = products;
        this.context = context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSanPham, tvGiaSanPham, tvThem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSanPham = itemView.findViewById(R.id.tvNameItemfl);
            tvTenSanPham = itemView.findViewById(R.id.tvPriceItemfl);
            tvTenSanPham = itemView.findViewById(R.id.tvThem);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_item_for_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = flashSaleList.get(position);
        holder.tvTenSanPham.setText(product.getName());
        holder.tvGiaSanPham.setText(product.getPrice().toString());

    }

    @Override
    public int getItemCount() {
        return flashSaleList.size();
    }

}
