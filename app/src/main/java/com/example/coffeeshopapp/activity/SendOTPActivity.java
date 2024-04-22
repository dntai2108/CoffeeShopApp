package com.example.coffeeshopapp.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivitySendOtpactivityBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    private ActivitySendOtpactivityBinding binding;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendOtpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }


    private void setEven() {
        binding.btnNhanMaOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtSoDienThoai.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SendOTPActivity.this, "Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                if (binding.edtSoDienThoai.getText().toString().trim().length() != 9) {
                    Toast.makeText(SendOTPActivity.this, "Số điện thoại không đúng định dạng", Toast.LENGTH_SHORT).show();
                    binding.edtSoDienThoai.requestFocus();
                    return;
                }
                binding.pbXuLy.setVisibility(VISIBLE);
                binding.btnNhanMaOTP.setVisibility(View.INVISIBLE);
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        binding.pbXuLy.setVisibility(GONE);
                        binding.btnNhanMaOTP.setVisibility(VISIBLE);
                        Toast.makeText(SendOTPActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        binding.pbXuLy.setVisibility(VISIBLE);
                        binding.btnNhanMaOTP.setVisibility(VISIBLE);
                        Toast.makeText(SendOTPActivity.this, "Mã OTP đã được gửi thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SendOTPActivity.this, OTPVerificationActivity.class);
                        intent.putExtra("phone", binding.edtSoDienThoai.getText().toString());
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                    }
                };
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + binding.edtSoDienThoai.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(SendOTPActivity.this)
                        .setCallbacks(mCallbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }
}
