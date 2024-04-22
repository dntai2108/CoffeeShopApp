package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityBottomNavBinding;
import com.example.coffeeshopapp.fragment.Fragment_donhang;
import com.example.coffeeshopapp.fragment.Fragment_mon;
import com.example.coffeeshopapp.fragment.Fragment_taikhoan;
import com.example.coffeeshopapp.fragment.Fragment_trangchu;

public class Bottom_nav extends AppCompatActivity {
    private ActivityBottomNavBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=ActivityBottomNavBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        if (savedInstanceState == null) {
            replaceFragment(new Fragment_trangchu());
        }
        bd.navView.setOnItemSelectedListener(item -> {
            int itemId=item.getItemId();
            if(itemId==R.id.bottomnav_trangchu){
                replaceFragment(new Fragment_trangchu());
            }
            if(itemId==R.id.bottomnav_mon){
                replaceFragment(new Fragment_mon());
            }
            if(itemId==R.id.bottomnav_donhang){
                replaceFragment(new Fragment_donhang());
            }
            if(itemId==R.id.bottomnav_taikhoan){
                replaceFragment(new Fragment_taikhoan());
            }
           return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }
}