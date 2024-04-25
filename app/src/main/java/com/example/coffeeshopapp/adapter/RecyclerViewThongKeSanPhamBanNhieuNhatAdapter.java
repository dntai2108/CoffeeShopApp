package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RecyclerViewThongKeSanPhamBanNhieuNhatAdapter extends
        RecyclerView.Adapter<RecyclerViewThongKeSanPhamBanNhieuNhatAdapter.ViewHolder>{
    FirebaseStorage storage = FirebaseStorage.getInstance();
     private final ArrayList<Product> productArrayList;
    private final Context context;

    public RecyclerViewThongKeSanPhamBanNhieuNhatAdapter(ArrayList<Product> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSanPham;
        TextView tvNameProduct,tvPrice,tvQuatity,tvStt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgSanPham=itemView.findViewById(R.id.imgSanPham);
            tvStt=itemView.findViewById(R.id.tvStt);
            tvNameProduct=itemView.findViewById(R.id.tvNameProduct);
            tvQuatity=itemView.findViewById(R.id.tvQuatity);
            tvPrice=itemView.findViewById(R.id.tvPrice);
        }
    }
    @NonNull
    @Override
    public RecyclerViewThongKeSanPhamBanNhieuNhatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.layout_item_thongke_sanpham_mua_nhieunhat,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewThongKeSanPhamBanNhieuNhatAdapter.ViewHolder holder, int position) {
        Product product= productArrayList.get(position);
        StorageReference storageRef = storage.getReference();
        // Load ảnh vào ImageView bằng Glide
        storageRef.child(product.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.imgSanPham);
            }
        });
        // làm số thu tu
        holder.tvStt.setText(String.valueOf(position+1));
        holder.tvPrice.setText(product.getPrice());
        holder.tvNameProduct.setText(product.getName());
        holder.tvQuatity.setText(product.getSoluongmua());

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }


}
