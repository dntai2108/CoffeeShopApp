package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivitySuccessNotifyShipperBinding;

public class SuccessNotifyActivityShipper extends AppCompatActivity {
    private ActivitySuccessNotifyShipperBinding bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivitySuccessNotifyShipperBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setEvent();
    }

    private void setEvent() {
        bd.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessNotifyActivityShipper.this,OrderShipper.class);
                startActivity(intent);
            }
        });
    }
}