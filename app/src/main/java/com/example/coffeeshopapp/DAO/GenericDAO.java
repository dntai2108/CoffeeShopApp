package com.example.coffeeshopapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T> extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DatabaseAppCoffee.db";
    private static final int DATABASE_VERSION = 1;

    public GenericDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Phương thức để thực hiện CRUD
    public abstract void add(T item);
    //public abstract Cursor getAll();
    public abstract Cursor getAll();
    public abstract void update(T item);
    public abstract void delete(long id);







    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
