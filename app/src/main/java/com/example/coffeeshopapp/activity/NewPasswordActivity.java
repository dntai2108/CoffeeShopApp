package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.coffeeshopapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class NewPasswordActivity extends AppCompatActivity {

    ImageView ivQuayLai;
    TextInputEditText edtMatKhauMoi, edtXacNhanMatKhau;

    AppCompatButton btnXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        setControl();
        setEven();
    }

    private void setControl() {
        ivQuayLai = findViewById(R.id.ivQuayLai);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }

    private void setEven() {

    }
}