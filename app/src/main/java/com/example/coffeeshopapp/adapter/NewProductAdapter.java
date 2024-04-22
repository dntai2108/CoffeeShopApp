package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Product;

import java.util.ArrayList;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {

    private ArrayList<Product> productList;
    private Context context;

    public void setData(ArrayList<Product> list) {
        this.productList = list;
        notifyDataSetChanged();
    }

    public NewProductAdapter(Context context) {
        this.context = context;
    }

    public NewProductAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvMonMoi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvMonMoi = itemView.findViewById(R.id.rvMonMoi);
        }

    }

    @NonNull
    @Override
    public NewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_home, parent, false);
        return new NewProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.rvMonMoi.setLayoutManager(linearLayoutManager);
//        ItemAdapter itemAdapter = new ItemAdapter();
//        holder.rvMonMoi.setAdapter(itemAdapter);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
