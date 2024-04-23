package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.coffeeshopapp.databinding.ActivityAccountAdminBinding;

public class AccountActivityAdmin extends AppCompatActivity {
    private ActivityAccountAdminBinding bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityAccountAdminBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setEvent();
    }

    private void setEvent() {
        bd.tvManageCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivityAdmin.this,ManageUser.class);
                startActivity(intent);
                finish();
            }
        });

        bd.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivityAdmin.this, ProfileActivity.class));
            }
        });

        bd.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivityAdmin.this, LoginActivity.class));
                finish();
            }
        });

        bd.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivityAdmin.this, ManageProductActivity.class));
            }
        });
    }
}