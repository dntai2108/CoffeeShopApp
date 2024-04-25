package com.example.coffeeshopapp.activity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityLoginBinding;
import com.example.coffeeshopapp.model.Account;
import com.example.coffeeshopapp.model.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    String userId = "";

    private ActivityLoginBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }


    protected void setEven() {
        binding.btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soDienThoai = binding.edtSoDienThoai.getText().toString();
                String matKhau = binding.edtMatKhau.getText().toString();
                if (TextUtils.isEmpty(soDienThoai)) {
                    Toast.makeText(LoginActivity.this, "Số điện thoại không được trống", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.setError("Nhập số điện thoại: ");
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(matKhau)) {
                    Toast.makeText(LoginActivity.this, "Mật khẩu không được trống", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtMatKhau.requestFocus();
                    return;
                } else {
                    databaseReference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.hasChild(soDienThoai)) {
                                Toast.makeText(LoginActivity.this, "Số điện thoại chưa được đăng ký", Toast.LENGTH_SHORT).show();
                                binding.edtSoDienThoai.requestFocus();
                                return;
                            } else if (!snapshot.child(soDienThoai).child("state").getValue(Boolean.class)) {
                                Toast.makeText(LoginActivity.this, "Tài khoản của bạn đã bị khóa! Liên hệ CSKH để được hỗ trợ!", Toast.LENGTH_SHORT).show();
                                binding.edtSoDienThoai.requestFocus();
                                return;
                            } else {

                                String layMatKhau = snapshot.child(soDienThoai).child("password").getValue(String.class);
                                String role = snapshot.child(soDienThoai).child("role").getValue(String.class);
                                DatabaseReference customerRef = databaseReference.child("Customer");
                                customerRef.addValueEventListener(new ValueEventListener() {


                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Customer customer = dataSnapshot.getValue(Customer.class);
                                            if (customer.getAccount().getUsername().equals(soDienThoai)) {
                                                userId = customer.getId();
                                                SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor edittor = sharedPreferences.edit();
                                                edittor.putString("phone", soDienThoai);
                                                edittor.putString("password", matKhau);
                                                edittor.putString("userId", userId);
                                                edittor.commit();
                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                if (layMatKhau.equals(matKhau)) {
                                    binding.pbXuLy.setVisibility(VISIBLE);
                                    binding.btnDangNhap.setVisibility(INVISIBLE);
                                    if (binding.cbLuuDangNhap.isChecked()) {
                                        GhiMatKhau();
                                    } else {
                                        KhongGhiMatKhau();
                                    }
                                    if (role.equals("user")) {
                                        Intent intent = new Intent(LoginActivity.this, Bottom_nav.class);
                                        startActivity(intent);
                                    } else if (role.equals("admin")) {
                                        Intent intent = new Intent(LoginActivity.this, BottomNavAdmin.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, BottomNavigationActivityShipper.class);
                                        startActivity(intent);
                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                    binding.edtMatKhau.requestFocus();
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

        binding.tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SendOTPActivity.class);
                startActivity(intent);
            }
        });

        binding.tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");
        binding.edtSoDienThoai.setText(phone);
        binding.edtMatKhau.setText(password);
    }

    private void KhongGhiMatKhau() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    private void GhiMatKhau() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", binding.edtSoDienThoai.getText().toString());
        editor.putString("password", binding.edtMatKhau.getText().toString());
        editor.apply();
    }


}