package com.example.coffeeshopapp.activity;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityOtpverificationBinding;
import com.example.coffeeshopapp.model.Account;
import com.example.coffeeshopapp.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class OTPVerificationActivity extends AppCompatActivity {
    private ActivityOtpverificationBinding binding;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpverificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }


    private void setEven() {
        String soDienThoai = getIntent().getStringExtra("phone");
        String sDT = "0" + soDienThoai;
        binding.tvSoDienThoai.setText(String.format("+84-%s", soDienThoai));
        String verificationId = getIntent().getStringExtra("verificationId");
        String hoTen = getIntent().getStringExtra("name");
        String matKhau = getIntent().getStringExtra("password");
        String email = getIntent().getStringExtra("email");
        String direction = getIntent().getStringExtra("direction");


        binding.tvGuiLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OTPVerificationActivity.this, "Mã OTP đã được gửi lại!", Toast.LENGTH_SHORT).show();
            }
        });
        focusOTP();
        binding.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.pbXuLy.setVisibility(VISIBLE);
                binding.btnXacNhan.setVisibility(INVISIBLE);
                if (binding.edtOTP1.getText().toString().trim().isEmpty() ||
                        binding.edtOTP2.getText().toString().trim().isEmpty() ||
                        binding.edtOTP3.getText().toString().trim().isEmpty() ||
                        binding.edtOTP4.getText().toString().trim().isEmpty() ||
                        binding.edtOTP5.getText().toString().trim().isEmpty() ||
                        binding.edtOTP6.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OTPVerificationActivity.this, "Mã OTP không được trống", Toast.LENGTH_SHORT).show();
                }
                if (verificationId != null) {
                    String code = binding.edtOTP1.getText().toString().trim()
                            + binding.edtOTP2.getText().toString().trim()
                            + binding.edtOTP3.getText().toString().trim()
                            + binding.edtOTP4.getText().toString().trim()
                            + binding.edtOTP5.getText().toString().trim()
                            + binding.edtOTP6.getText().toString().trim();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String node = UUID.randomUUID().toString();
                                        Account account = new Account(sDT, matKhau, "user", true);
                                        Customer customer = new Customer(node, hoTen, "", email, sDT, "", account);
                                        databaseReference.child("Account").child(sDT).setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                        databaseReference.child("Customer").child(node).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edittor = sharedPreferences.edit();
                                        edittor.putString("phone", sDT);
                                        edittor.commit();
                                        if (direction.equals("register")) {
                                            Intent intent = new Intent(OTPVerificationActivity.this, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        if (direction.equals("forget")) {
                                            Intent intent = new Intent(OTPVerificationActivity.this, NewPasswordActivity.class);
                                            startActivity(intent);
                                        }

                                    } else {
                                        binding.pbXuLy.setVisibility(GONE);
                                        binding.btnXacNhan.setVisibility(VISIBLE);
                                        Toast.makeText(OTPVerificationActivity.this, "OTP không chính xác", Toast.LENGTH_SHORT).show();
                                        binding.edtOTP1.setText("");
                                        binding.edtOTP2.setText("");
                                        binding.edtOTP3.setText("");
                                        binding.edtOTP4.setText("");
                                        binding.edtOTP5.setText("");
                                        binding.edtOTP6.setText("");
                                        binding.edtOTP1.requestFocus();

                                    }
                                }
                            });
                }
            }
        });

    }

    private void focusOTP() {
        binding.edtOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.edtOTP2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.edtOTP3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.edtOTP4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.edtOTP5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtOTP5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.edtOTP6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}