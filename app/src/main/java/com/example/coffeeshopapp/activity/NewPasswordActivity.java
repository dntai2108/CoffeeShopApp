package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityNewPasswordBinding;
import com.example.coffeeshopapp.model.Account;
import com.example.coffeeshopapp.model.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewPasswordActivity extends AppCompatActivity {

    private ActivityNewPasswordBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }


    private void setEven() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String matKhau = binding.edtMatKhauMoi.getText().toString();
        String xacNhanMatKhau = binding.edtXacNhanMatKhau.getText().toString();
        binding.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(matKhau)) {
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhauMoi.setError("Nhập mật khẩu: ");
                    binding.edtMatKhauMoi.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(xacNhanMatKhau)) {
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu xác nhận không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                if (!matKhau.equals(xacNhanMatKhau)) {
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                DatabaseReference accountRef = databaseReference.child("Account");
                DatabaseReference customerRef = databaseReference.child("Customer");
                customerRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Customer customer = dataSnapshot.getValue(Customer.class);
                            Account account = customer.getAccount();
                            if (customer.getAccount().getUsername().equals(phone)) {
                                account.setPassword(matKhau);
                                customer.setAccount(account);
                                accountRef.child(phone).child("password").setValue(matKhau);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}