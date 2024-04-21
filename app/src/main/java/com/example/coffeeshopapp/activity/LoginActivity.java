package com.example.coffeeshopapp.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityLoginBinding;
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
    private ActivityLoginBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

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
                            } else {
                                String layMatKhau = snapshot.child(soDienThoai).child("matKhau").getValue(String.class);
                                String sDT = soDienThoai.substring(1);
                                if (layMatKhau.equals(matKhau)) {
                                    binding.pbXuLy.setVisibility(VISIBLE);
                                    binding.btnDangNhap.setVisibility(View.INVISIBLE);
                                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            binding.pbXuLy.setVisibility(GONE);
                                            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationId,
                                                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                            binding.pbXuLy.setVisibility(VISIBLE);
                                            Intent intent = new Intent(LoginActivity.this, OTPVerificationActivity.class);
                                            intent.putExtra("phone", sDT);
                                            intent.putExtra("verificationId", verificationId);
                                            startActivity(intent);
                                        }
                                    };
                                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                                            .setPhoneNumber("+84" + sDT)
                                            .setTimeout(60L, TimeUnit.SECONDS)
                                            .setActivity(LoginActivity.this)
                                            .setCallbacks(mCallbacks)
                                            .build();
                                    PhoneAuthProvider.verifyPhoneNumber(options);

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
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
}