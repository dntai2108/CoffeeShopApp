package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityBottomNavBinding;
import com.example.coffeeshopapp.fragment.Fragment_donhang;
import com.example.coffeeshopapp.fragment.Fragment_giohang;
import com.example.coffeeshopapp.fragment.Fragment_taikhoan;
import com.example.coffeeshopapp.fragment.Fragment_trangchu;

public class Bottom_nav extends AppCompatActivity {
    private ActivityBottomNavBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottomnav_trangchu) {
                replaceFragment(new Fragment_trangchu());
            }
            if (itemId == R.id.bottomnav_giohang) {
                replaceFragment(new Fragment_giohang());
            }
            if (itemId == R.id.bottomnav_donhang) {
                replaceFragment(new Fragment_donhang());
            }
            if (itemId == R.id.bottomnav_taikhoan) {
                replaceFragment(new Fragment_taikhoan());
            }
            return true;
        });
        if (savedInstanceState == null) {
            Boolean openTaiKhoan = getIntent().getBooleanExtra("openTaiKhoan", false);
            String role = getIntent().getStringExtra("role");
            if (openTaiKhoan) {
                if (role.equals("admin")) {
                    Intent intent = new Intent(Bottom_nav.this, BottomNavAdmin.class);
                    intent.putExtra("openTaiKhoan", true);
                    startActivity(intent);
                    return;
                }
                if (role.equals("shipper")) {
                    Intent intent = new Intent(Bottom_nav.this, BottomNavigationActivityShipper.class);
                    intent.putExtra("openTaiKhoan", true);
                    startActivity(intent);
                    return;
                }
                replaceFragment(new Fragment_taikhoan());
                bd.navView.getMenu().findItem(R.id.bottomnav_taikhoan).setChecked(true);
            } else {
                replaceFragment(new Fragment_trangchu());

            }
        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}