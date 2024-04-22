package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.coffeeshopapp.Adapter.RecycleViewManageOrderAdapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityManageOrderBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManageOrder extends AppCompatActivity {
    private ActivityManageOrderBinding bd;
    private ArrayList<Order> orderList;
    private RecycleViewManageOrderAdapter recycleViewManageOrderAdapter;

    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);
        bd = ActivityManageOrderBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setData();
        setControl();
        setEven();
    }

    private void setData() {
        //Spinner
        String[] items = {"Tất cả", "Chờ duyệt", "Chuẩn bị đơn hàng", "Đang giao", "Hoàn thành", "Đã hủy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bd.SpinnerOder.setAdapter(adapter);
        bd.SpinnerOder.setOnItemSelectedListener(spinnerListener);
        this.orderList = new ArrayList<>();
        reloadOrder();
        bd.RecyclerViewOrder.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recycleViewManageOrderAdapter = new RecycleViewManageOrderAdapter(orderList,this);
        bd.RecyclerViewOrder.setAdapter(recycleViewManageOrderAdapter);
    }

    private void setControl() {
        bd.toolbarmnguser.setTitle("QUẢN LÝ ORDER");
        setSupportActionBar(bd.toolbarmnguser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setEven() {
    }

    public void reloadOrder(){
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    for(DataSnapshot orderSnapshot: dataSnapshot.child("Order").getChildren()){
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
                        Date date;
                        try {
                            date  =  dateFormat.parse(orderDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Date now =  new Date();
                        long diffInMillis = now.getTime() - date .getTime();
                        long diffInHours = diffInMillis / (1000 * 60 * 60);
                        if(diffInHours <= 24){
                            orderList.add(o);
                        }
                    }
                }
                recycleViewManageOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void reloadOrderWithCondition(String trangThai){
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    for(DataSnapshot orderSnapshot: dataSnapshot.child("Order").getChildren()){
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
                        Date date;
                        try {
                            date  =  dateFormat.parse(orderDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Date now =  new Date();
                        long diffInMillis = now.getTime() - date .getTime();
                        long diffInHours = diffInMillis / (1000 * 60 * 60);
                        if(diffInHours <= 24 && status.equals(trangThai)){
                            orderList.add(o);
                        }
                    }
                }
                recycleViewManageOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();
            if(selectedItem.equals("Tất cả")){
                reloadOrder();
            }
            else if(selectedItem.equals("Chờ duyệt")){
                reloadOrderWithCondition("Chờ duyệt");
            }
            else if(selectedItem.equals("Chuẩn bị đơn hàng")){
                reloadOrderWithCondition("Chuẩn bị đơn hàng");
            }
            else if(selectedItem.equals("Đang giao")){
                reloadOrderWithCondition("Đang giao");
            }
            else if(selectedItem.equals("Hoàn thành")){
                reloadOrderWithCondition("Hoàn thành");
            }
            else if(selectedItem.equals("Đã hủy")){
                reloadOrderWithCondition("Đã hủy");
            }
            recycleViewManageOrderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}