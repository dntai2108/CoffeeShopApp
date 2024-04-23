package com.example.coffeeshopapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DatabaseAppCoffee.db";
    private static final int DATABASE_VERSION = 1;

    private final Context mContext;




    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không cần thực hiện gì ở đây nếu bạn đã có cơ sở dữ liệu sẵn trong assets

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Thực hiện nâng cấp cơ sở dữ liệu khi có phiên bản mới
    }





}
