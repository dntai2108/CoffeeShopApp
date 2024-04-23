package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.coffeeshopapp.adapter.RecycleViewOrderShipperAdapter;
import com.example.coffeeshopapp.databinding.ActivityOrderShipperBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderShipper extends AppCompatActivity {
    private ActivityOrderShipperBinding bd;
    private ArrayList<Order> orderList;
    private RecycleViewOrderShipperAdapter recycleViewOrderShipperAdapter;

    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityOrderShipperBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setData();
        setEvent();
    }

    private void setEvent() {
    }

    private void setData() {
    }

    public void reloadOrder(){
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: dataSnapshot.getChildren()){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Order o = new Order();
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderId = orderSnapshot.child("orderId").getValue(String.class);
                        String status = orderSnapshot.child("status").getValue(String.class);
                        String totalAmount = orderSnapshot.child("totalAmount").getValue(String.class);
                        o.setOrderId(orderId);
                        o.setOrderDate(orderDate);
                        o.setStatus(status);
                        o.setTotalAmount(totalAmount);
                        if(o.getStatus().equalsIgnoreCase("Chờ duyệt")){
                            orderList.add(o);
                        }
                    }
                    recycleViewOrderShipperAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}