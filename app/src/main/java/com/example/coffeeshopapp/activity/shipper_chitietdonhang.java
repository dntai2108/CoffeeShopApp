package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.ChitietdondathangdadatAdapter;
import com.example.coffeeshopapp.adapter.RecycleViewShipperChiTietDonHang;
import com.example.coffeeshopapp.databinding.ActivityChitietDonhangDadatBinding;
import com.example.coffeeshopapp.databinding.ActivityShipperChitietdonhangBinding;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class shipper_chitietdonhang extends AppCompatActivity {
    private ActivityShipperChitietdonhangBinding bd;
    private RecyclerView recyclerView;

    private RecycleViewShipperChiTietDonHang recycleViewShipperChiTietDonHang;
    private ArrayList<Cart> carttemList;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Customer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityShipperChitietdonhangBinding.inflate(getLayoutInflater());
        boolean flag = getIntent().getBooleanExtra("flag",false);
        if(flag){
            bd.btnInShipperChiTietDonHang.setText("Hoàn thành");
            bd.btnHuy.setText("Hủy");
        }
        else{
            bd.btnInShipperChiTietDonHang.setText("Giao Hàng");
            bd.btnHuy.setText("Quay Lại");
        }
        setContentView(bd.getRoot());
        recyclerView = findViewById(R.id.recyclerviewdetaiorder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        carttemList = new ArrayList<>();
        recycleViewShipperChiTietDonHang = new RecycleViewShipperChiTietDonHang(carttemList, this);
        recyclerView.setAdapter(recycleViewShipperChiTietDonHang);
        setEvent();
        layThongTinDiaChi();
        layThongTinDonHang();
        fetchDataFromFirebase();
    }

    private void setEvent() {
        bd.btnInShipperChiTietDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = getIntent().getBooleanExtra("flag",false);
                String maDonHang = getIntent().getStringExtra("maDonHang");
                if(flag){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                                for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                                    if(orderSnapshot.getKey().equals(maDonHang)){
                                        orderSnapshot.getRef().child("status").setValue("Hoàn thành");
                                        Toast.makeText(shipper_chitietdonhang.this,"Hoàn thành đơn hàng", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(shipper_chitietdonhang.this, SuccessNotifyActivityShipper.class);
                                        startActivity(intent);
                                        databaseReference.removeEventListener(this);
                                        onBackPressed();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                                for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                                    if(orderSnapshot.getKey().equals(maDonHang)){
                                        orderSnapshot.getRef().addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.child("status").getRef().setValue("Đang giao");
                                                Toast.makeText(shipper_chitietdonhang.this,"đang giao đơn hàng", Toast.LENGTH_SHORT).show();
                                                orderSnapshot.getRef().removeEventListener(this);
                                                databaseReference.removeEventListener(this);
                                                onBackPressed();
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        bd.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = getIntent().getBooleanExtra("flag",false);
                String maDonHang = getIntent().getStringExtra("maDonHang");
                if(flag){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                                for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                                    if(orderSnapshot.getKey().equals(maDonHang)){
                                        orderSnapshot.getRef().child("status").setValue("Đã Hủy");
                                        Toast.makeText(shipper_chitietdonhang.this,"đã hủy đơn hàng", Toast.LENGTH_LONG).show();
                                        databaseReference.removeEventListener(this);
                                        return;
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    onBackPressed();
                }
            }
        });
    }

    private void fetchDataFromFirebase() {
        String maDonHang = getIntent().getStringExtra("maDonHang");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                        if(orderSnapshot.getKey().equals(maDonHang)){
                            orderSnapshot.getRef().child("cartList").addValueEventListener(new ValueEventListener() {
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
                                        recycleViewShipperChiTietDonHang.notifyDataSetChanged();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(shipper_chitietdonhang.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layThongTinDonHang() {
        String maDonHang = getIntent().getStringExtra("maDonHang");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                        if(orderSnapshot.getKey().equals(maDonHang)){
                            orderSnapshot.getRef().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(shipper_chitietdonhang.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layThongTinDiaChi() {

        // Lấy thông tin địa chỉ
        String maDonHang = getIntent().getStringExtra("maDonHang");
//        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference("Customer")
//                .child("Customer123").child("Order").child(maDonHang).child("customer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                        if(orderSnapshot.getKey().equals(maDonHang)){
                            orderSnapshot.getRef().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(shipper_chitietdonhang.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}