package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityProfileShipperBinding;
import com.example.coffeeshopapp.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileShipperActivity extends AppCompatActivity {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ActivityProfileShipperBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileShipperBinding.inflate(getLayoutInflater());
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
                Intent intent = new Intent(ProfileShipperActivity.this, ChangeInfoShipper.class);
                startActivity(intent);
            }
        });
        binding.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileShipperActivity.this, BottomNavigationActivityShipper.class);
                intent.putExtra("openTaiKhoan", true);
                startActivity(intent);

            }
        });


    }
}