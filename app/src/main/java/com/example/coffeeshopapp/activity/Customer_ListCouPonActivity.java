package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.Admin_RecyclerViewListCouponAdapter;
import com.example.coffeeshopapp.adapter.Customer_RecyclerViewListCouPonAdapter;
import com.example.coffeeshopapp.databinding.ActivityCustomerListCouPonBinding;
import com.example.coffeeshopapp.model.Coupon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Customer_ListCouPonActivity extends AppCompatActivity {
    private ActivityCustomerListCouPonBinding bd;
    ArrayList<Coupon> couponArrayList;
    Customer_RecyclerViewListCouPonAdapter couponAdapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coupon");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=ActivityCustomerListCouPonBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.recyclerViewListCoupon.setHasFixedSize(true);
        bd.recyclerViewListCoupon.setLayoutManager(new LinearLayoutManager(this));
        couponArrayList = new ArrayList<>();
        couponAdapter = new Customer_RecyclerViewListCouPonAdapter(Customer_ListCouPonActivity.this, couponArrayList);
        bd.recyclerViewListCoupon.setAdapter(couponAdapter);

        setEvent();
    }

    private void setEvent() {
        bd.btnBack.setOnClickListener(v -> finish());
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
    }
}