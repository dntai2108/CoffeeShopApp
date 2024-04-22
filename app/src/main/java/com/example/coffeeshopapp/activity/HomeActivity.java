package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.ItemAdapter;
import com.example.coffeeshopapp.adapter.NewProductAdapter;
import com.example.coffeeshopapp.databinding.ActivityHomeBinding;
import com.example.coffeeshopapp.model.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Product> productList;
    private ItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productList = new ArrayList<>();
        reloadProduct();
        setEven();
    }

    private void setEven() {
        binding.bnvMenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottomnav_taikhoan) {
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
            }
            return true;
        });

        binding.rvMonMoi.setHasFixedSize(true);
        binding.rvMonMoi.setLayoutManager(new GridLayoutManager(this, 2));
        itemAdapter = new ItemAdapter(productList, this);
        binding.rvMonMoi.setAdapter(itemAdapter);

    }

    private void reloadProduct() {

        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                itemAdapter.setData(productList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}