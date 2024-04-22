package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.coffeeshopapp.adapter.ProductAdapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityManageProductBinding;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ManageProductActivity extends AppCompatActivity {
    private ActivityManageProductBinding bd;
    private List<Product> listProduct;
    private ProductAdapter productAdapter;
    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Product");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
        bd = ActivityManageProductBinding.inflate(getLayoutInflater());
//        dbHelper = new CreateDatabase(ManageProductActivity.this);
//        copyDatabase();
        setContentView(bd.getRoot());
        setData();
        setControl();
        setEven();
    }

    private void setData() {
        this.listProduct = new ArrayList<>();
        this.productAdapter = new ProductAdapter(this, R.layout.activity_item_product, listProduct);
        bd.listViewProduct.setAdapter(productAdapter);
    }


    private void setEven() {
        reloadProduct();
        bd.imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageProductActivity.this, AddProduct.class);
                startActivity(intent);
            }
        });

        bd.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                databaseReferences.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listProduct.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Product product = dataSnapshot.getValue(Product.class);
                            if (product.getName().toLowerCase().contains(newText.toLowerCase())) {
                                listProduct.add(product);
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }
        });
    }
//    public void copyDatabase() {
//        try {
//            InputStream inputStream = getAssets().open("DatabaseAppCoffee.db");
//            Log.d("TAG", "copyDatabase: thành công đúng k1");
//            FileOutputStream fileOutputStream = new FileOutputStream(getDatabasePath("DatabaseAppCoffee.db"));
//
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                fileOutputStream.write(buffer, 0, length);
//            }
//
//            fileOutputStream.flush();
//            fileOutputStream.close();
//            inputStream.close();
//
//        } catch (IOException e) {
//            Log.d("TAG", "copyDatabase: thành công đúng k");
//            Log.d("TAG", e.getMessage());
//            e.printStackTrace();
//        }
//        Log.d("TAG", "copyDatabase: thành công");
//    }


    @Override
    protected void onResume() {
        super.onResume();
        reloadProduct();
    }

    private void setControl() {
        bd.toolbarmngproduct.setTitle("COFFEE");
        setSupportActionBar(bd.toolbarmngproduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void reloadProduct() {
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProduct.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    listProduct.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}