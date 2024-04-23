package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coffeeshopapp.adapter.RecycleViewAdapterProduct;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ListProductActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecycleViewAdapterProduct adapter;
    ArrayList<Product> datalist;

    ImageView imgcart;

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        recyclerView = findViewById(R.id.recyclerViewListproduct);
        recyclerView.setHasFixedSize(true);
        // số cột là 2
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        datalist = new ArrayList<>();
        adapter = new RecycleViewAdapterProduct(datalist, this);
        recyclerView.setAdapter(adapter);
        setEvent();
    }

    private void setEvent() {
        //Lấy dữ liệu product từ firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    datalist.add(product);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        // nút giỏ hàng dùng img
        imgcart = findViewById(R.id.imgCart);
        imgcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Customer")
                        .child("Customer123").child("Cart");
                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Nếu có dữ liệu trong giỏ hàng, chuyển sang màn hình giỏ hàng
                            Intent intent = new Intent(ListProductActivity.this, CartActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } else {
                            // Nếu không có dữ liệu trong giỏ hàng, thông báo cho người dùng
                            Toast.makeText(ListProductActivity.this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra
                        Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage());
                        Toast.makeText(ListProductActivity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

