package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.ChitietdondathangdadatAdapter;
import com.example.coffeeshopapp.databinding.ActivityChitietDonhangDadatAdminBinding;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chitiet_donhang_dadat_activity_admin extends AppCompatActivity {
    public interface YesNoDialogListener {
        void onYesClicked();

        void onNoClicked();
    }
    private ActivityChitietDonhangDadatAdminBinding bd;
    private RecyclerView recyclerView;
    private ChitietdondathangdadatAdapter recyclerViewDetaiOrderAdapter;
    private ArrayList<Cart> carttemList;
    // Nhận mã đơn hàng từ Intent
    private String maDonHang = "";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityChitietDonhangDadatAdminBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        recyclerView = findViewById(R.id.recyclerviewdetaiorder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        carttemList = new ArrayList<>();
        recyclerViewDetaiOrderAdapter = new ChitietdondathangdadatAdapter(carttemList, this);
        recyclerView.setAdapter(recyclerViewDetaiOrderAdapter);
        setEvent();
        layThongTinDiaChi();
        layThongTinDonHang();
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        maDonHang = getIntent().getStringExtra("madonhang");
        String userId = getIntent().getStringExtra("customerId");
        databaseReference.child("Customer").child(userId).child("Order").child(maDonHang).child("cartList").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carttemList.clear();
                // Kiểm tra xem nếu có dữ liệu trong giỏ hàng của khách hàng
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hiển thị thông báo lỗi
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage());
                Toast.makeText(Chitiet_donhang_dadat_activity_admin.this, "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layThongTinDonHang() {
        maDonHang = getIntent().getStringExtra("madonhang");
        String userId = getIntent().getStringExtra("customerId");
        // Lấy thông tin địa chỉ
        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference("Customer")
                .child(userId).child("Order").child(maDonHang);
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
                Toast.makeText(Chitiet_donhang_dadat_activity_admin.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layThongTinDiaChi() {
        maDonHang = getIntent().getStringExtra("madonhang");
        String userId = getIntent().getStringExtra("customerId");
        // Lấy thông tin địa chỉ
        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference().child("Customer");
        customerInfoRef.child(userId).child("Order").child(maDonHang).child("customer").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    bd.tvSdt.setText(phone);
                } else {
                    bd.tvaddress.setText("Rỗng");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(Chitiet_donhang_dadat_activity_admin.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvent() {
        boolean flag = getIntent().getBooleanExtra("flag",false);
        if(flag){
            bd.btnHuy.setVisibility(View.VISIBLE);
        }
        else{
            bd.btnHuy.setVisibility(View.GONE);
        }
        bd.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYesNoDialog(new Chitiet_donhang_dadat_activity_admin.YesNoDialogListener(){

                    @Override
                    public void onYesClicked() {
                        String maDonHang = getIntent().getStringExtra("madonhang");
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Customer");
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot orderSnapshot : customerSnapshot.child("Order").getChildren()) {
                                        if (orderSnapshot.getKey().equals(maDonHang)) {
                                            orderSnapshot.getRef().child("status").setValue("Đã hủy");
                                            Toast.makeText(Chitiet_donhang_dadat_activity_admin.this, "Hủy đơn hàng thành công", Toast.LENGTH_LONG).show();
                                            bd.btnHuy.setVisibility(View.GONE);
                                            databaseReference1.removeEventListener(this);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNoClicked() {
                        return;
                    }
                });
            }
        });

        bd.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }
    private void showYesNoDialog(final Chitiet_donhang_dadat_activity_admin.YesNoDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn tiếp tục không?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onYesClicked();
                        dialog.dismiss(); // Đóng hộp thoại
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNoClicked();
                        dialog.dismiss(); // Đóng hộp thoại
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}