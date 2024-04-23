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

public class Admin_RecyclerViewListCouponAdapter extends RecyclerView.Adapter<Admin_RecyclerViewListCouponAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Coupon> couponArrayList;

    public Admin_RecyclerViewListCouponAdapter(Context context, ArrayList<Coupon> couponArrayList) {
        this.context = context;
        this.couponArrayList = couponArrayList;
    }
    public interface OnDeleteItemClickListener {
        void onDeleteItem(int position);
    }
    private OnDeleteItemClickListener mListener1;

    public void setOnDeleteItemClickListener(OnDeleteItemClickListener mListener) {
        mListener1 = mListener;
    }

    @NonNull
    @Override
    public Admin_RecyclerViewListCouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.custom_item_coupon,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin_RecyclerViewListCouponAdapter.ViewHolder holder, int position) {
        Coupon coupon=couponArrayList.get(position);
        holder.tvCoupon.setText(coupon.getTenMa());
        holder.tvPhantramgiam.setText(coupon.getPhanTramGiam());
        holder.tvThoigianbatdau.setText(coupon.getNgayBatDau());
        holder.tvThoigianketthuc.setText(coupon.getNgayKetThuc());
        holder.btnXoamagiamgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener1 != null) {
                    // Lấy vị trí của mục trong danh sách
                    int position = holder.getAdapterPosition();
                    // Kiểm tra xem vị trí hợp lệ
                    if (position != RecyclerView.NO_POSITION) {
                        // Gọi phương thức onDeleteItemClick từ mListener và truyền vị trí của mục
                        mListener1.onDeleteItem(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCoupon,tvThoigianbatdau,tvThoigianketthuc,tvPhantramgiam;
        Button btnXoamagiamgia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoupon=itemView.findViewById(R.id.tvCoupon);
            tvThoigianbatdau=itemView.findViewById(R.id.tvThoigianbatdau);
            tvThoigianketthuc=itemView.findViewById(R.id.tvThoigianketthuc);
            tvPhantramgiam=itemView.findViewById(R.id.tvPhantramgiam);
            btnXoamagiamgia=itemView.findViewById(R.id.btnXoamagiamgia);

        }
    }
}
