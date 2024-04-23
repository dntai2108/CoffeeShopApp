package com.example.coffeeshopapp.activity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityRegisterBinding;
import com.example.coffeeshopapp.model.Account;
import com.example.coffeeshopapp.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private boolean isExist = false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }

    private void setEven() {
        binding.tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
                    Toast.makeText(RegisterActivity.this, "Tên tài khoản không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtHoTen.setError("Nhập tài khoản: ");
                    binding.edtHoTen.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Email không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtEmail.setError("Nhập email: ");
                    binding.edtEmail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                    binding.edtEmail.setError("Nhập email: ");
                    binding.edtEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(soDienThoai)) {
                    Toast.makeText(RegisterActivity.this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.setError("Nhập số điện thoại: ");
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                if (soDienThoai.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.setError("Nhập số điện thoại: ");
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(matKhau)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtMatKhau.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(xacNhanMatKhau)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                if (!matKhau.equals(xacNhanMatKhau)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                if (checkPhoneExist(soDienThoai)) {
                    Toast.makeText(RegisterActivity.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.setError("Nhập số điện thoại: ");
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }

                String sDT = soDienThoai.substring(1);
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        binding.pbXuLy.setVisibility(GONE);
                        binding.btnDangKy.setVisibility(VISIBLE);
                        Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        binding.pbXuLy.setVisibility(VISIBLE);
                        binding.btnDangKy.setVisibility(INVISIBLE);
                        Intent intent = new Intent(RegisterActivity.this, OTPVerificationActivity.class);
                        intent.putExtra("phone", sDT);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("name", hoTen);
                        intent.putExtra("email", email);
                        intent.putExtra("password", matKhau);
                        intent.putExtra("direction", "register");
                        startActivity(intent);
                    }
                };
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + sDT)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(RegisterActivity.this)
                        .setCallbacks(mCallbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);


            }
        });
    }

    private Boolean checkPhoneExist(String phone) {

        databaseReference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isExist = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isExist;
    }


}