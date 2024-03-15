package com.example.coffeeshopapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshopapp.R;

public class WelcomeActivity extends AppCompatActivity {
    Button btnKhamPha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setControl();
        setEven();
    }

    protected void setControl(){
        btnKhamPha = findViewById(R.id.btnKhamPha);
    }
    protected void setEven(){
        btnKhamPha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}