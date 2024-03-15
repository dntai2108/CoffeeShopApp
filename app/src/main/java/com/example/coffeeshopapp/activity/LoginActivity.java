package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopapp.R;

public class LoginActivity extends AppCompatActivity {
    EditText edtTaiKhoan, edtMatKhau;
    TextView tvQuenMatKhau, tvDangKy;
    AppCompatButton btnDangNhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEven();
    }

    protected void setControl(){
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
        tvDangKy = findViewById(R.id.tvDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);

    }

    protected void setEven(){
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTaiKhoan.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Tên tài khoản không được trống", Toast.LENGTH_SHORT).show();
                    edtTaiKhoan.setError("Nhập tài khoản: ");
                    edtTaiKhoan.requestFocus();
                    return;
                }
                if(edtMatKhau.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Mật khẩu không được trống", Toast.LENGTH_SHORT).show();
                    edtMatKhau.setError("Nhập mật khẩu: ");
                    edtMatKhau.requestFocus();
                    return;
                }

            }
        });

        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


    }
}