package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityBottomNavigationShipperBinding;
import com.example.coffeeshopapp.fragment.FragmentAccountActivityShipper;
import com.example.coffeeshopapp.fragment.FragmentAccountAdmin;
import com.example.coffeeshopapp.fragment.FragmentOrderShipper;
import com.example.coffeeshopapp.fragment.Fragment_Deliveried;
import com.example.coffeeshopapp.fragment.Fragment_Delivering;
import com.example.coffeeshopapp.fragment.Fragment_DeliveryOrderShipper;
import com.example.coffeeshopapp.fragment.Fragment_LichSuGiaoHang;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationActivityShipper extends AppCompatActivity {
    private ActivityBottomNavigationShipperBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityBottomNavigationShipperBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.Order) {
                replaceFragment(new FragmentOrderShipper());
            } else if (itemId == R.id.Activity) {
                replaceFragment(new Fragment_DeliveryOrderShipper());
            } else if (itemId == R.id.lichsugiaohang) {
                replaceFragment(new Fragment_LichSuGiaoHang());
            } else if (itemId == R.id.Account) {
                replaceFragment(new FragmentAccountActivityShipper());
            }
            return true;
        });
        if (savedInstanceState == null) {
            Boolean openTaiKhoan = getIntent().getBooleanExtra("openTaiKhoan", false);
            if (openTaiKhoan) {
                replaceFragment(new FragmentAccountActivityShipper());
                bd.bottomNav.getMenu().findItem(R.id.Account).setChecked(true);
                return;
            }
            replaceFragment(new FragmentOrderShipper());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayout, fragment);
        fragmentTransaction.commit();
    }
}