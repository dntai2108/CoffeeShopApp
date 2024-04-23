package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;


import com.example.coffeeshopapp.model.Coupon;

import java.util.ArrayList;

public class RecyclerViewListCouponAdapter extends RecyclerView.Adapter<RecyclerViewListCouponAdapter.ViewHolder>{
    Context context;
    ArrayList<Coupon> arrayListCoupon;

    public RecyclerViewListCouponAdapter(Context context, ArrayList<Coupon> arrayListCoupon) {
        this.context = context;
        this.arrayListCoupon = arrayListCoupon;
    }
    // dùng để xóa mã giảm giá trong list
    public interface OnDeleteItemMagiamgiaClickListener {
        void onDeleteItemClick(int position);
    }

    private RecyclerViewListCouponAdapter.OnDeleteItemMagiamgiaClickListener mListener;

    public void OnDeleteItemMagiamgiaClickListener(RecyclerViewListCouponAdapter.OnDeleteItemMagiamgiaClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView magiamgia,thoigianbatdau,thoigianketthuc,phantramgiam;
        Button xoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            magiamgia=itemView.findViewById(R.id.tvCoupon);
            thoigianbatdau=itemView.findViewById(R.id.tvThoigianbatdau);
            thoigianketthuc=itemView.findViewById(R.id.tvThoigianketthuc);
            phantramgiam=itemView.findViewById(R.id.tvPhantramgiam);
            xoa=itemView.findViewById(R.id.btnXoamagiamgia);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_item_coupon,parent,false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coupon coupon=arrayListCoupon.get(position);
        holder.magiamgia.setText(coupon.getTenMa());
        holder.thoigianbatdau.setText(coupon.getNgayBatDau());
        holder.thoigianketthuc.setText(coupon.getNgayKetThuc());
        holder.phantramgiam.setText(coupon.getPhanTramGiam());
        holder.xoa.setOnClickListener(v -> {
            if (mListener != null) {
                // Lấy vị trí của mục trong danh sách
                int position1 = holder.getAdapterPosition();
                // Kiểm tra xem vị trí hợp lệ
                if (position1 != RecyclerView.NO_POSITION) {
                    // Gọi phương thức onDeleteItemClick từ mListener và truyền vị trí của mục
                    mListener.onDeleteItemClick(position1);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return arrayListCoupon.size();
    }
}
