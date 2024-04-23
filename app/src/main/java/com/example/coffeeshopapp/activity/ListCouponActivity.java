package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.coffeeshopapp.adapter.RecyclerViewListCouponAdapter;
import com.example.coffeeshopapp.databinding.ActivityListCouponBinding;
import com.example.coffeeshopapp.model.Coupon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ListCouponActivity extends AppCompatActivity implements RecyclerViewListCouponAdapter.OnDeleteItemMagiamgiaClickListener{
    private ActivityListCouponBinding bd;
    ArrayList<Coupon> couponArrayList;
    RecyclerViewListCouponAdapter couponAdapter;
    final private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Coupon");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=ActivityListCouponBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.recyclerViewListCoupon.setHasFixedSize(true);
        bd.recyclerViewListCoupon.setLayoutManager(new LinearLayoutManager(this));
        couponArrayList= new ArrayList<>();
        couponAdapter = new RecyclerViewListCouponAdapter(ListCouponActivity.this,couponArrayList);
        bd.recyclerViewListCoupon.setAdapter(couponAdapter);
        setEvent();

    }

    private void setEvent() {
        //Lấy list mã giảm giá
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                couponArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren() ){
                    Coupon coupon=dataSnapshot.getValue(Coupon.class);
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
                Intent intent= new Intent(ListCouponActivity.this,AddCouponActivity.class);
                startActivity(intent);
            }
        });


    }
    // Xóa mã giảm giá khỏi list
   /* @Override
    public void OnDeleteItemMagiamgiaClickListener(int position) {
        Coupon deleteItem = couponArrayList.get(position);
        String CouponId = deleteItem.getId(); // Lấy id sản phẩm
        //  final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
        //            .getReference("Customer").child("Customer123").child("Cart");
        DatabaseReference itemRef = databaseReference.child(CouponId);
        itemRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xoá thành công từ Firebase, cập nhật lại danh sách và tính lại tổng giá tiền
                couponArrayList.remove(position);
                couponAdapter.notifyItemRemoved(position);

                Toast.makeText(ListCouponActivity.this, "Xoá mã giảm giá thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Xảy ra lỗi khi xoá từ Firebase
                Toast.makeText(ListCouponActivity.this, "Lỗi khi xoá mã giảm giá: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
*/
    @Override
    public void onDeleteItemClick(int position) {
        Coupon deleteItem = couponArrayList.get(position);
        String CouponId = deleteItem.getId(); // Lấy id sản phẩm
        //  final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
        //            .getReference("Customer").child("Customer123").child("Cart");
        DatabaseReference itemRef = databaseReference.child(CouponId);
        itemRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Xoá thành công từ Firebase, cập nhật lại danh sách và tính lại tổng giá tiền
                couponArrayList.remove(position);
                couponAdapter.notifyItemRemoved(position);

                Toast.makeText(ListCouponActivity.this, "Xoá mã giảm giá thành công", Toast.LENGTH_SHORT).show();
            } else {
                // Xảy ra lỗi khi xoá từ Firebase
                Toast.makeText(ListCouponActivity.this, "Lỗi khi xoá mã giảm giá: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}