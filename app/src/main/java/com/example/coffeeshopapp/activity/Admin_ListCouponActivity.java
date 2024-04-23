package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.coffeeshopapp.adapter.Admin_RecyclerViewListCouponAdapter;
import com.example.coffeeshopapp.databinding.ActivityListCouponBinding;
import com.example.coffeeshopapp.model.Coupon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_ListCouponActivity extends AppCompatActivity implements Admin_RecyclerViewListCouponAdapter.OnDeleteItemClickListener {
    private ActivityListCouponBinding bd;
    ArrayList<Coupon> couponArrayList;
    Admin_RecyclerViewListCouponAdapter couponAdapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coupon");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityListCouponBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.recyclerViewListCoupon.setHasFixedSize(true);
        bd.recyclerViewListCoupon.setLayoutManager(new LinearLayoutManager(this));
        couponArrayList = new ArrayList<>();
        couponAdapter = new Admin_RecyclerViewListCouponAdapter(Admin_ListCouponActivity.this, couponArrayList);
        bd.recyclerViewListCoupon.setAdapter(couponAdapter);
        couponAdapter.setOnDeleteItemClickListener(this);
        setEvent();

    }

    private void setEvent() {
        //Lấy list mã giảm giá
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                couponArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Coupon coupon = dataSnapshot.getValue(Coupon.class);
                    couponArrayList.add(coupon);
                }
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Nút thêm mã giảm giá
        bd.btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_ListCouponActivity.this, Admin_AddCouponActivity.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public void onDeleteItem(int position) {
        // Xóa mã giảm giá tại vị trí position khỏi danh sách
        Coupon deletedCoupon = couponArrayList.remove(position);
        couponAdapter.notifyItemRemoved(position);

        // Cập nhật dữ liệu trong cơ sở dữ liệu của bạn
        // Ví dụ: Firebase Realtime Database
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Coupon").child(deletedCoupon.getId());
        couponRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Xóa thành công từ Firebase
                    Toast.makeText(Admin_ListCouponActivity.this, "Đã xóa mã giảm giá", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi xóa từ Firebase
                    Toast.makeText(Admin_ListCouponActivity.this, "Lỗi khi xóa mã giảm giá: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}