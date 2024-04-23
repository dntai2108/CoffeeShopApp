package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;


import com.example.coffeeshopapp.Adapter.RecyclerViewListCouponAdapter;
import com.example.coffeeshopapp.databinding.ActivityListCouponBinding;
import com.example.coffeeshopapp.model.Coupon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCouponActivity extends AppCompatActivity {
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

    }
}