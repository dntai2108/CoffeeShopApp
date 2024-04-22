package com.example.coffeeshopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Cart;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<Cart> cartItemList;
    private Context context;
    public CartItemAdapter(List<Cart> cartItemList, Context context) {
        this.cartItemList = cartItemList;

        this.context = context;

    }
    // dùng để xóa sản phẩm trong giỏ
    public interface OnDeleteItemClickListener {
        void onDeleteItemClick(int position);
    }
    private OnDeleteItemClickListener mListener;
    public void setOnDeleteItemClickListener(OnDeleteItemClickListener listener) {
        mListener = listener;
    }
    // dùng để tăng giảm số lượng sản phẩm trong giỏ
    // Interface để xử lý sự kiện khi người dùng thay đổi số lượng sản phẩm
    public interface OnQuantityChangeListener {
        void onIncreaseQuantity(int position);
        void onDecreaseQuantity(int position);
    }

    private OnQuantityChangeListener qListener;// số lượng

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        qListener = listener;
    }

    // Phương thức để xử lý sự kiện khi người dùng nhấn vào nút tăng số lượng sản phẩm
    private void increaseQuantity(int position) {
        if (qListener != null) {
            qListener.onIncreaseQuantity(position);
        }
    }

    // Phương thức để xử lý sự kiện khi người dùng nhấn vào nút giảm số lượng sản phẩm
    private void decreaseQuantity(int position) {
        if (qListener != null) {
            qListener.onDecreaseQuantity(position);
        }
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        Cart cartItem = cartItemList.get(position);
        holder.tvProductName.setText(cartItem.getProductimgurl().getName());
        holder.tvProductPrice.setText(cartItem.getProductimgurl().getPrice());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.size.setText(cartItem.getSize());
        // Sử dụng Glide để tải hình ảnh và thiết lập vào ImageView
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getProductimgurl().getImgurl()) // Thay thế "getImageUrl()" bằng phương thức lấy URL của hình ảnh từ đối tượng Productimgurl của bạn
                .into(holder.imgproductflc);
       // xử lí xóa sản phẩm trong giỏ
        holder.btnDeleteproductflc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    // Lấy vị trí của mục trong danh sách
                    int position = holder.getAdapterPosition();
                    // Kiểm tra xem vị trí hợp lệ
                    if (position != RecyclerView.NO_POSITION) {
                        // Gọi phương thức onDeleteItemClick từ mListener và truyền vị trí của mục
                        mListener.onDeleteItemClick(position);
                    }
                }
            }
        });
        // xử lí tăng giảm số lượng
        holder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity(holder.getAdapterPosition());
            }
        });
        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        ImageView imgproductflc;
        TextView size;
        Button btnDeleteproductflc;
        ImageView btnTang,btnGiam;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvNameProductflc);
            tvProductPrice = itemView.findViewById(R.id.tvPriceProductflc);
            tvQuantity = itemView.findViewById(R.id.tvQuantityofProductflc);
            imgproductflc=itemView.findViewById(R.id.imgflc);
            btnDeleteproductflc=itemView.findViewById(R.id.btnDeleteflc);
            btnTang=itemView.findViewById(R.id.imgPlusflc);
            btnGiam=itemView.findViewById(R.id.imgMinusflc);
            size=itemView.findViewById(R.id.tvSizeProductflc);


        }
    }
}
