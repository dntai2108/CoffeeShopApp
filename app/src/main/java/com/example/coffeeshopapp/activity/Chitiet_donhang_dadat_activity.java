package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.coffeeshopapp.adapter.ChitietdondathangdadatAdapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityChitietDonhangDadatBinding;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chitiet_donhang_dadat_activity extends AppCompatActivity {
    private ActivityChitietDonhangDadatBinding bd;
    private RecyclerView recyclerView;
    private ChitietdondathangdadatAdapter recyclerViewDetaiOrderAdapter;
    private ArrayList<Cart> carttemList;
    // Nhận mã đơn hàng từ Intent
    private String maDonHang = getIntent().getStringExtra("madonhang");
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("Customer").child("Customer123").child("Order").child(maDonHang).child("cartList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityChitietDonhangDadatBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        maDonHang = getIntent().getStringExtra("madonhang");

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerviewdetaiorder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        carttemList = new ArrayList<>();
        recyclerViewDetaiOrderAdapter = new ChitietdondathangdadatAdapter(carttemList, this);
        recyclerView.setAdapter(recyclerViewDetaiOrderAdapter);
        layThongTinDiaChi();
        layThongTinDonHang();
        fetchDataFromFirebase();
    }

    private void layThongTinDiaChi() {

        // Lấy thông tin địa chỉ
        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference("Customer")
                .child("Customer123").child("Order").child(maDonHang).child("customer");
        customerInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem có dữ liệu tồn tại không
                if (snapshot.exists()) {
                    // Lấy địa chỉ từ dữ liệu
                    String address = snapshot.child("address").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    //Set data cho textview
                    bd.tvaddress.setText(address);
                    bd.tvName.setText(name);
                    bd.tvsdt.setText(phone);
                } else {
                    bd.tvaddress.setText("Rỗng");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(Chitiet_donhang_dadat_activity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layThongTinDonHang() {

        // Lấy thông tin địa chỉ
        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference("Customer")
                .child("Customer123").child("Order").child(maDonHang);
        customerInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem có dữ liệu tồn tại không
                if (snapshot.exists()) {
                    // Lấy địa chỉ từ dữ liệu
                    String orderDate = snapshot.child("orderDate").getValue(String.class);
                    String orderId = snapshot.child("orderId").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String totalAmount = snapshot.child("totalAmount").getValue(String.class);
                    //Set data cho textview
                    bd.tvorderid.setText(orderId);
                    bd.tvorderdate.setText(orderDate);
                    bd.tvStatus.setText(status);
                    bd.tvPrice.setText(totalAmount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(Chitiet_donhang_dadat_activity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carttemList.clear();
                // Kiểm tra xem nếu có dữ liệu trong giỏ hàng của khách hàng
                if (snapshot.exists()) {
                    // Duyệt qua tất cả các nút con (các sản phẩm) trong giỏ hàng
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        // Lấy thông tin về sản phẩm
                        Product product = productSnapshot.child("product").getValue(Product.class);
                        String quantity = productSnapshot.child("quantity").getValue(String.class);
                        String size = productSnapshot.child("size").getValue(String.class);
                        Cart cart = new Cart(product, quantity, size);
                        carttemList.add(cart);
                    }
                    recyclerViewDetaiOrderAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hiển thị thông báo lỗi
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage());
                Toast.makeText(Chitiet_donhang_dadat_activity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }// end fetch data from firebase
}