package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.adapter.ChitietdondathangdadatAdapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityChitietDonhangDadatBinding;
import com.example.coffeeshopapp.fragment.Fragment_donhang;
import com.example.coffeeshopapp.fragment.Fragment_giohang;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chitiet_donhang_dadat_activity extends AppCompatActivity {
    public interface YesNoDialogListener {
        void onYesClicked();

        void onNoClicked();
    }

    private ActivityChitietDonhangDadatBinding bd;
    private RecyclerView recyclerView;
    private ChitietdondathangdadatAdapter recyclerViewDetaiOrderAdapter;
    private ArrayList<Cart> carttemList;
    // Nhận mã đơn hàng từ Intent
    private String maDonHang = "";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityChitietDonhangDadatBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerviewdetaiorder);
        ImageView btnBack = findViewById(R.id.btnBack);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        carttemList = new ArrayList<>();
        recyclerViewDetaiOrderAdapter = new ChitietdondathangdadatAdapter(carttemList, this);
        recyclerView.setAdapter(recyclerViewDetaiOrderAdapter);
        setEvent();
        layThongTinDiaChi();
        layThongTinDonHang();
        fetchDataFromFirebase();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, new Fragment_donhang());
                fragmentTransaction.commit();
                finish();
            }
        });

    }

    private void setEvent() {
        bd.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYesNoDialog(new YesNoDialogListener() {
                    @Override
                    public void onYesClicked() {
                        String maDonHang = getIntent().getStringExtra("madonhang");
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Customer");
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot customerSnapshot : snapshot.getChildren()) {
                                    for (DataSnapshot orderSnapshot : customerSnapshot.child("Order").getChildren()) {
                                        orderSnapshot.getRef().child("status").setValue("Đã hủy");
                                        Toast.makeText(Chitiet_donhang_dadat_activity.this, "Hủy đơn hàng thành công", Toast.LENGTH_LONG).show();
                                        databaseReference1.removeEventListener(this);
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
    }

    private void layThongTinDiaChi() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        maDonHang = sharedPreferences.getString("orderid", "");
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
                Toast.makeText(Chitiet_donhang_dadat_activity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layThongTinDonHang() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        maDonHang = sharedPreferences.getString("orderid", "");
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
                Toast.makeText(Chitiet_donhang_dadat_activity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showYesNoDialog(final Chitiet_donhang_dadat_activity.YesNoDialogListener listener) {
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

    private void fetchDataFromFirebase() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        maDonHang = sharedPreferences.getString("orderid", "");
        databaseReference.child("Customer").child(userId).child("Order").child(maDonHang).child("cartList").addValueEventListener(new ValueEventListener() {
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