package com.example.coffeeshopapp.activity;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivitySignUpBinding;
import com.example.coffeeshopapp.databinding.ActivityWelcomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }


    private void setEven() {

        binding.tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        binding.btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.pbXuLy.setVisibility(VISIBLE);
                String hoTen = binding.edtHoTen.getText().toString();
                String email = binding.edtEmail.getText().toString();
                String soDienThoai = binding.edtSoDienThoai.getText().toString();
                String matKhau = binding.edtMatKhau.getText().toString();
                String xacNhanMatKhau = binding.edtXacNhanMatKhau.getText().toString();

                if (TextUtils.isEmpty(hoTen)) {
                    Toast.makeText(SignUpActivity.this, "Tên tài khoản không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtHoTen.setError("Nhập tài khoản: ");
                    binding.edtHoTen.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Email không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtEmail.setError("Nhập email: ");
                    binding.edtEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignUpActivity.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    binding.edtEmail.setError("Nhập email: ");
                    binding.edtEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(soDienThoai)) {
                    Toast.makeText(SignUpActivity.this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.setError("Nhập số điện thoại: ");
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                if (soDienThoai.length() != 10) {
                    Toast.makeText(SignUpActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.setError("Nhập số điện thoại: ");
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(matKhau)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtMatKhau.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(xacNhanMatKhau)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu xác nhận không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                if (!matKhau.equals(xacNhanMatKhau)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                } else {
                    databaseReference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Nếu số điện thoại tồn tại
                            if (snapshot.hasChild(soDienThoai)) {
                                Toast.makeText(SignUpActivity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("Account").child(soDienThoai).child("matKhau").setValue(matKhau);
                                databaseReference.child("Account").child(soDienThoai).child("role").setValue("User");
                                Toast.makeText(SignUpActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(soDienThoai)) {
                                Toast.makeText(SignUpActivity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = UUID.randomUUID().toString();
                                databaseReference.child("Customer").child(userId).child("hoTen").setValue(hoTen);
                                databaseReference.child("Customer").child(userId).child("email").setValue(email);
                                databaseReference.child("Customer").child(userId).child("gender").setValue("");
                                databaseReference.child("Customer").child(userId).child("phone").setValue(soDienThoai);
                                databaseReference.child("Customer").child(userId).child("address").setValue("");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }


}