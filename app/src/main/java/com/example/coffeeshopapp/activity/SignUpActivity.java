package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coffeeshopapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {
    EditText edtTaiKhoan, edtEmail, edtSoDienThoai;
    TextInputEditText edtMatKhau, edtXacNhanMatKhau;
    AppCompatButton btnDangKy;
    TextView tvDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setControl();
        setEven();
    }



    private void setControl(){
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        btnDangKy = findViewById(R.id.btnDangKy);
        tvDangNhap = findViewById(R.id.tvDangNhap);
    }
    private void setEven() {

        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maDienThoai = edtSoDienThoai.getText().toString();
                String maEmail = edtEmail.getText().toString();

                // Mở OPT Activity với email và số điện thoại
                Intent intent = new Intent(SignUpActivity.this, OTPVerificationActivity.class);
                intent.putExtra("soDienThoai", maDienThoai);
                intent.putExtra("email", maEmail);
                startActivity(intent);
            }
        });
    }
}