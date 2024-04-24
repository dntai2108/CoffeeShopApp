package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Cart;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewShipperChiTietDonHang extends RecyclerView.Adapter<RecycleViewShipperChiTietDonHang.OrderItemViewHolder>{

    private List<Cart> cartItemList;
    private Context context;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public RecycleViewShipperChiTietDonHang(List<Cart> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
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

    @NonNull
    @Override
    public RecycleViewShipperChiTietDonHang.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_cart, parent, false);
        return new RecycleViewShipperChiTietDonHang.OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewShipperChiTietDonHang.OrderItemViewHolder holder, int position) {
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
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
