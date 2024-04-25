package com.example.coffeeshopapp.activity;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityBottomNavBinding;
import com.example.coffeeshopapp.fragment.Fragment_donhang;
import com.example.coffeeshopapp.fragment.Fragment_giohang;
import com.example.coffeeshopapp.fragment.Fragment_taikhoan;
import com.example.coffeeshopapp.fragment.Fragment_trangchu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bottom_nav extends AppCompatActivity {
    private ActivityBottomNavBinding bd;
    private DatabaseReference cartRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        bd.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottomnav_trangchu) {
                replaceFragment(new Fragment_trangchu());
            }
            if (itemId == R.id.bottomnav_giohang) {
                replaceFragment(new Fragment_giohang());
                //checkCart();
               /* // Lấy tham chiếu đến nút "Cart" của người dùng hiện tại
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    cartRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).child("Cart");
                }
                if (cartRef != null) {
                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Kiểm tra nếu giỏ hàng rỗng
                            if (!dataSnapshot.exists()) {
                                // Hiển thị thông báo nếu giỏ hàng rỗng
                                Toast.makeText(Bottom_nav.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Giỏ hàng có ít nhất một mặt hàng
                                replaceFragment(new Fragment_giohang());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
                            Toast.makeText(Bottom_nav.this, "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
*/

            }

            if (itemId == R.id.bottomnav_donhang) {
                replaceFragment(new Fragment_donhang());
            }
            if (itemId == R.id.bottomnav_taikhoan) {
                replaceFragment(new Fragment_taikhoan());
            }
            return true;
        });
        if (savedInstanceState == null) {
            Boolean openTaiKhoan = getIntent().getBooleanExtra("openTaiKhoan", false);
            String role = getIntent().getStringExtra("role");
            if (openTaiKhoan) {
                if (role.equals("admin")) {
                    Intent intent = new Intent(Bottom_nav.this, BottomNavAdmin.class);
                    intent.putExtra("openTaiKhoan", true);
                    startActivity(intent);
                    return;
                }
                if (role.equals("shipper")) {
                    Intent intent = new Intent(Bottom_nav.this, BottomNavigationActivityShipper.class);
                    intent.putExtra("openTaiKhoan", true);
                    startActivity(intent);
                    return;
                }
                replaceFragment(new Fragment_taikhoan());
                bd.navView.getMenu().findItem(R.id.bottomnav_taikhoan).setChecked(true);
            } else {
                replaceFragment(new Fragment_trangchu());
            }
        }

    }

    private void checkCart() {
        // Lấy tham chiếu đến nút "Cart" của người dùng hiện tại
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            cartRef = FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).child("Cart");
        }
        if (cartRef != null) {
            cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Kiểm tra nếu giỏ hàng rỗng
                    if (!dataSnapshot.exists()) {
                        // Hiển thị thông báo nếu giỏ hàng rỗng
                        Toast.makeText(Bottom_nav.this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Giỏ hàng có ít nhất một mặt hàng, thay thế fragment "Giỏ hàng"
                        replaceFragment(new Fragment_giohang());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
                    Toast.makeText(Bottom_nav.this, "Đã xảy ra lỗi khi đọc dữ liệu từ Firebase.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}