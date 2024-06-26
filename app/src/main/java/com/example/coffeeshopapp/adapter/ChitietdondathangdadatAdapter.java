package com.example.coffeeshopapp.adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.net.Uri;
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
import com.example.coffeeshopapp.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChitietdondathangdadatAdapter extends RecyclerView.Adapter<ChitietdondathangdadatAdapter.OrderItemViewHolder> {
    private List<Cart> cartItemList;
    private Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    public ChitietdondathangdadatAdapter(List<Cart> cartItemList, Context context) {
        this.cartItemList = cartItemList;

        this.context = context;

    }


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChitietdondathangdadatAdapter.OrderItemViewHolder holder, int position) {
        Cart cartItem = cartItemList.get(position);
        holder.tvProductName.setText(cartItem.getProduct().getName());
        holder.tvProductPrice.setText(cartItem.getProduct().getPrice());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        
        StorageReference storageRef = storage.getReference();
        storageRef.child(cartItem.getProduct().getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(holder.imgproductflc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String a = e.getMessage();
                Log.e("ImageDownload", "Error downloading image: " + e.getMessage());
            }
        });

    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        ImageView imgproductflc;
        TextView size;
        Button btnDeleteproductflc;
        ImageView btnTang, btnGiam;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvNameProductflc);
            tvProductPrice = itemView.findViewById(R.id.tvPriceProductflc);
            tvQuantity = itemView.findViewById(R.id.tvQuantityofProductflc);
            imgproductflc = itemView.findViewById(R.id.imgflc);
            btnDeleteproductflc = itemView.findViewById(R.id.btnDeleteflc);
            btnTang = itemView.findViewById(R.id.imgPlusflc);
            btnGiam = itemView.findViewById(R.id.imgMinusflc);
            size = itemView.findViewById(R.id.tvSizeProductflc);


        }
    }
}
