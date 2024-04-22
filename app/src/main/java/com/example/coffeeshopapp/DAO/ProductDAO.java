package com.example.coffeeshopapp.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coffeeshopapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends GenericDAO<Product> {
    public ProductDAO(Context context) {
        super(context);
    }

    private static final String TABLE_NAME = "Product";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NameProduct";
    private static final String COLUMN_IMAGE = "Image";
    private static final String COLUMN_PRICE = "PriceProduct";

    @Override
    public void add(Product item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NameProduct", item.getName());
        values.put("Image", item.getImage());
        values.put("PriceProduct", item.getPrice());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    @Override
    public Cursor getAll() {

        SQLiteDatabase db = getReadableDatabase();

        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select NameProduct,PriceProduct from Product", null);
        /*if (cursor != null && cursor.moveToFirst()) {
            do {

              *//*  String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));*//*

                Product product=new Product();
                product.setName((cursor.getString(1)));
                product.setImage((cursor.getString(2)));
                product.setPrice((cursor.getDouble(3)));
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return productList;*/
        return cursor;
    }

    @Override
    public void update(Product item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_IMAGE, item.getImage());
        values.put(COLUMN_PRICE, item.getPrice());
        // db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    @Override
    public void delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<byte[]> getAllImages() {
        SQLiteDatabase db = getReadableDatabase();
        List<byte[]> imageList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IMAGE + " FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                imageList.add(imageBytes);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return imageList;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }
}
