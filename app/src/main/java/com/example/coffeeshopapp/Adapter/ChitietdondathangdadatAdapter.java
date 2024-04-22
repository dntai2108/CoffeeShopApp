package com.example.coffeeshopapp.adapter;

import android.content.Context;
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

import java.util.List;

public class ChitietdondathangdadatAdapter extends RecyclerView.Adapter<ChitietdondathangdadatAdapter.OrderItemViewHolder> {
    private List<Cart> cartItemList;
    private Context context;

    public ChitietdondathangdadatAdapter(List<Cart> cartItemList, Context context) {
        this.cartItemList = cartItemList;

        this.context = context;

    }


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_cart, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChitietdondathangdadatAdapter.OrderItemViewHolder holder, int position) {
        Cart cartItem = cartItemList.get(position);
        holder.tvProductName.setText(cartItem.getProduct().getName());
        holder.tvProductPrice.setText(cartItem.getProduct().getPrice());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Sử dụng Glide để tải hình ảnh và thiết lập vào ImageView
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProduct().getImage()) // Thay thế "getImageUrl()" bằng phương thức lấy URL của hình ảnh từ đối tượng Productimgurl của bạn
                .into(holder.imgproductflc);

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
