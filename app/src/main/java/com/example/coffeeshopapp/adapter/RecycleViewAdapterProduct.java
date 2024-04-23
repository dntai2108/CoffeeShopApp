package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.DetailProductActivity;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;


public class RecycleViewAdapterProduct extends RecyclerView.Adapter<RecycleViewAdapterProduct.ViewHolder> {
    private final ArrayList<Product> productList;
    private final Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public RecycleViewAdapterProduct(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        ImageView hinh;
        ImageView hinhcart;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Khởi tạo các thành phần của ViewHolder
            textViewName = itemView.findViewById(R.id.tvNameItemfl);
            textViewPrice = itemView.findViewById(R.id.tvPriceItemfl);
            hinh = itemView.findViewById(R.id.imgItemfl);
            button = itemView.findViewById(R.id.btnAddItem);
            hinhcart = itemView.findViewById(R.id.imgCart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_for_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewName.setText(product.getName());
        holder.textViewPrice.setText(product.getPrice());
        // Log đường dẫn ảnh
        Log.d("ProductImage", "Đường dẫn ảnh: " + product.getImage());
        StorageReference storageRef = storage.getReference();
        // Load ảnh vào ImageView bằng Glide
        storageRef.child(product.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(holder.hinh);
            }
        });
        // truyền sáng detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("product", (Parcelable) product);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}



