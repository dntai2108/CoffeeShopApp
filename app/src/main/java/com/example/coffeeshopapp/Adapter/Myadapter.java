package com.example.coffeeshopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.DataClass;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {
   // private ArrayList<Product> datalist;
    private ArrayList<DataClass> datalist;
    private Context context;

    public Myadapter(ArrayList<DataClass> datalist, Context context) {
        this.datalist = datalist;
        this.context = context;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView recyclerImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerImage=itemView.findViewById(R.id.recycleImage);
        }
    }
    @NonNull
    @Override
    public Myadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycle_time_for_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myadapter.MyViewHolder holder, int position) {
        Glide.with(context).load(datalist.get(position).getImgUrl()).into(holder.recyclerImage);

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
