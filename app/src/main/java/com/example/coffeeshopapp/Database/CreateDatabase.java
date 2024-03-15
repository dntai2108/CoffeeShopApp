package com.example.coffeeshopapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {

    Context mcontext;
    public CreateDatabase(Context context, int version) {
        super(context, "DatabaseAppCoffee.db", null, version);
        this.mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CheckDB(){
        SQLiteDatabase sqLiteDatabase = null;
    }
}
