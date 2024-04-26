package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityChangeInfoAdminBinding;
import com.example.coffeeshopapp.databinding.ActivityChangeInfoBinding;
import com.example.coffeeshopapp.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeInfoAdmin extends AppCompatActivity {

    private ActivityChangeInfoAdminBinding binding;
    private String gender = "";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInfoAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEven();
    }

    private void setEven() {

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");
        DatabaseReference customerReference = databaseReference.child("Customer");
        customerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if (customer.equals(null)) {
                        continue;
                    }
                    if (customer.getPhone().equals(phone)) {
                        binding.edtName.setText(customer.getName());
                        binding.tvEmail.setText(customer.getEmail());
                        binding.tvSoDienThoai.setText(phone);
                        if (customer.getGender().equals("Nam") || customer.getGender().equals("")) {
                            binding.rbMale.setChecked(true);
                        }
                        if (customer.getGender().equals("Nữ")) {
                            binding.rbMale.setChecked(true);
                        }
                        customerReference.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == binding.rbMale.getId()) {
                    gender = "Nam";
                } else if (checkedId == binding.rbFemale.getId()) {
                    gender = "Nữ";
                }
            }
        });
        binding.btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Customer customer = dataSnapshot.getValue(Customer.class);
                            if (customer.getPhone().equals(phone)) {
                                String id = customer.getId();
                                customer.setName(binding.edtName.getText().toString());
                                customer.setGender(gender);
                                customer.setPhone(binding.tvSoDienThoai.getText().toString());
                                customer.setEmail(binding.tvEmail.getText().toString());
                                customerReference.child(id).setValue(customer);
                                Toast.makeText(ChangeInfoAdmin.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                String role = getIntent().getStringExtra("role");
                                Intent intent = new Intent(ChangeInfoAdmin.this, ProfileAdminActivity.class);
                                startActivity(intent);
                                break;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChangeInfoAdmin.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChangeInfoAdmin.this, BottomNavAdmin.class);
                startActivity(intent);
            }
        });
    }
}