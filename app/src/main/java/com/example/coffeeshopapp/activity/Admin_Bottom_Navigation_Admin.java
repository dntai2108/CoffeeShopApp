package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityAdminBottomNavigationAdminBinding;
import com.example.coffeeshopapp.fragment.Fragment_Static;
import com.example.coffeeshopapp.fragment.Fragment_Thongke_Sanphan_ban_duoc_nhieunhat;

public class Admin_Bottom_Navigation_Admin extends AppCompatActivity {
    private ActivityAdminBottomNavigationAdminBinding bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=ActivityAdminBottomNavigationAdminBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        if (savedInstanceState == null) {
            replaceFragment(new Fragment_Static());
        }
        bd.navStaticAdmin.setOnItemSelectedListener(item -> {
            int itemId=item.getItemId();
            if(itemId==R.id.nav_order_admin){
                replaceFragment(new Fragment_Static());
            }
            if(itemId==R.id.nav_product_admin){
                replaceFragment(new Fragment_Thongke_Sanphan_ban_duoc_nhieunhat());
            }
            if(itemId==R.id.nav_revenue_admin){
//                replaceFragment(new Fragment_Static());
            }
            if(itemId==R.id.back){
                Intent intent = new Intent(Admin_Bottom_Navigation_Admin.this,BottomNavAdmin.class);
                startActivity(intent);
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