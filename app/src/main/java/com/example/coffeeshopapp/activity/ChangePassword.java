package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityChangePasswordBinding;
import com.example.coffeeshopapp.fragment.Fragment_taikhoan;
import com.example.coffeeshopapp.model.Account;
import com.example.coffeeshopapp.model.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassword extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }

    private void setEven() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");


        binding.btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matKhauCu = binding.edtMatKhauCu.getText().toString();
                String matKhauMoi = binding.edtMatKhauMoi.getText().toString();
                String xacNhanMatKhau = binding.edtXacNhanMatKhau.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhauCu.setError("Nhập mật khẩu cũ: ");
                    binding.edtMatKhauCu.requestFocus();
                    return;
                }
                if (!matKhauCu.equals(password)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu cũ không chính xác", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhauCu.setError("Nhập mật khẩu cũ: ");
                    binding.edtMatKhauCu.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(matKhauMoi)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtMatKhauMoi.setError("Nhập mật khẩu mới: ");
                    binding.edtMatKhauMoi.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(xacNhanMatKhau)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu xác nhận không được để trống", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập lại mật khẩu xác nhận: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                if (matKhauCu.equals(matKhauMoi)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu mới phải khác mật khẩu cũ", Toast.LENGTH_SHORT).show();
                    binding.edtXacNhanMatKhau.setError("Nhập lại mật khẩu: ");
                    binding.edtXacNhanMatKhau.requestFocus();
                    return;
                }
                if (!matKhauMoi.equals(xacNhanMatKhau)) {
                    Toast.makeText(ChangePassword.this, "Mật khẩu xác nhận không trùng khớp", Toast.LENGTH_SHORT).show();
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
                                account.setPassword(matKhauMoi);
                                customer.setAccount(account);
                                accountRef.child(phone).child("password").setValue(matKhauMoi);
                                customerRef.child(customer.getId()).child("account").setValue(account);
                                Toast.makeText(ChangePassword.this, "Mật khẩu đã được thay đổi thành công", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                String role = getIntent().getStringExtra("role");
                Intent intent = new Intent(ChangePassword.this, Bottom_nav.class);
                intent.putExtra("role",role);
                intent.putExtra("openTaiKhoan", true);
                startActivity(intent);
            }

        });

        binding.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = getIntent().getStringExtra("role");
                Intent intent = new Intent(ChangePassword.this, Bottom_nav.class);
                intent.putExtra("role",role);
                intent.putExtra("openTaiKhoan", true);
                startActivity(intent);
            }
        });


    }
}