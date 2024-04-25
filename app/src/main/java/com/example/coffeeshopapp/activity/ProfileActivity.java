package com.example.coffeeshopapp.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityProfileBinding;
import com.example.coffeeshopapp.fragment.Fragment_taikhoan;
import com.example.coffeeshopapp.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
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
                    if (customer.getPhone().equals(phone)) {
                        binding.tvName.setText(customer.getName());
                        binding.tvEmail.setText(customer.getEmail());
                        binding.tvSoDienThoai.setText(phone);
                        binding.tvGioiTinh.setText(customer.getGender());
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = getIntent().getStringExtra("role");
                Intent intent = new Intent(ProfileActivity.this, ChangeInfoActivity.class);
                if (role.equals("admin")) {
                    intent.putExtra("role", role);
                }
                if (role.equals("shipper")) {
                    intent.putExtra("role", role);
                }
                intent.putExtra("openTaiKhoan", true);
                startActivity(intent);
            }
        });
        binding.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = getIntent().getStringExtra("role");
                Intent intent = new Intent(ProfileActivity.this, Bottom_nav.class);
                if (role.equals("admin")) {
                    intent.putExtra("role", role);
                }
                if (role.equals("shipper")) {
                    intent.putExtra("role", role);
                }
                intent.putExtra("openTaiKhoan", true);
                startActivity(intent);

            }
        });


    }

}