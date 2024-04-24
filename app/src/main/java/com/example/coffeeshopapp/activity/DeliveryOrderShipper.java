package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityBottomNavBinding;
import com.example.coffeeshopapp.databinding.ActivityDeliveryOrderShipperBinding;
import com.example.coffeeshopapp.databinding.ActivityOrderShipperBinding;
import com.example.coffeeshopapp.fragment.Fragment_Deliveried;
import com.example.coffeeshopapp.fragment.Fragment_Delivering;

public class DeliveryOrderShipper extends AppCompatActivity {
    private ActivityDeliveryOrderShipperBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityDeliveryOrderShipperBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        if (savedInstanceState == null) {
            replaceFragment(new Fragment_Delivering());
        }
        bd.navshipper.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_danggiao) {
                replaceFragment(new Fragment_Delivering());
            }
            if (itemId == R.id.nav_dagiao) {
                replaceFragment(new Fragment_Deliveried());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutOrder, fragment);
        fragmentTransaction.commit();
    }
}