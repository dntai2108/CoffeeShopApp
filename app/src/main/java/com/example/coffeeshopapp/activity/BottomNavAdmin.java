package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityBottomNavAdminBinding;
import com.example.coffeeshopapp.fragment.FragmentAccountActivityShipper;
import com.example.coffeeshopapp.fragment.FragmentAccountAdmin;
import com.example.coffeeshopapp.fragment.FragmentOrderShipper;
import com.example.coffeeshopapp.fragment.Fragment_DeliveryOrderShipper;
import com.example.coffeeshopapp.fragment.Fragment_DonHangAdmin;
import com.example.coffeeshopapp.fragment.Fragment_HomeAdmin;
import com.example.coffeeshopapp.fragment.Fragment_taikhoan;

public class BottomNavAdmin extends AppCompatActivity {
    private ActivityBottomNavAdminBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityBottomNavAdminBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottomnav_trangchu) {
                replaceFragment(new Fragment_HomeAdmin());
            } else if (itemId == R.id.bottomnav_donhang) {
                replaceFragment(new Fragment_DonHangAdmin());
            } else if (itemId == R.id.bottomnav_thongke) {
                replaceFragment(new FragmentAccountActivityShipper());
            } else if (itemId == R.id.bottomnav_taikhoan) {
                replaceFragment(new FragmentAccountAdmin());
            }
            return true;
        });
        if (savedInstanceState == null) {
            Boolean openTaiKhoan = getIntent().getBooleanExtra("openTaiKhoan", false);
            if (openTaiKhoan) {
                replaceFragment(new FragmentAccountAdmin());
                bd.bottomNav.getMenu().findItem(R.id.bottomnav_taikhoan).setChecked(true);
                return;
            } else {
                replaceFragment(new Fragment_HomeAdmin());

            }
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayout, fragment);
        fragmentTransaction.commit();
    }
}