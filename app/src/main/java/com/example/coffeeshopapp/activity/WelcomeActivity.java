package com.example.coffeeshopapp.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshopapp.Database.CreateDatabase;
import com.example.coffeeshopapp.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        CreateDatabase dbHelper = new CreateDatabase(this,1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
    }
}