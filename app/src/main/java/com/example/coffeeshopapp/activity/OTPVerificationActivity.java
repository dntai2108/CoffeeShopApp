package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coffeeshopapp.R;

public class OTPVerificationActivity extends AppCompatActivity {

    TextView tvSoDienThoai, tvGuiLai, tvThoiGian;
    EditText edtOTP1, edtOTP2, edtOTP3, edtOTP4;
    AppCompatButton btnXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
    }

    private void setControl() {
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvThoiGian = findViewById(R.id.tvThoiGian);
        tvGuiLai = findViewById(R.id.tvGuiLai);
        edtOTP1 = findViewById(R.id.edtOTP1);
        edtOTP2 = findViewById(R.id.edtOTP2);
        edtOTP3 = findViewById(R.id.edtOTP3);
        edtOTP4 = findViewById(R.id.edtOTP4);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }

    private void setEven() {

    }
}