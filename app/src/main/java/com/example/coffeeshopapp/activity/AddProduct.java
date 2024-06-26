package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;


import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityAddProductBinding;
import com.example.coffeeshopapp.model.Product;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    String image = "";

    private ActivityAddProductBinding bd;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        bd = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setControl();
        setEvent();
    }

    private void setEvent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        bd.btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        bd.toolbarmngproduct.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bd.btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = bd.imgProduct.getDrawable();
                if (drawable == null) {
                    Toast.makeText(AddProduct.this, "Vui lòng thêm hình ảnh", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bd.edtTenSP.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddProduct.this, "Vui lòng nhập tên sản phẩm", Toast.LENGTH_LONG).show();
                    return;
                }
                if (bd.edtGia.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddProduct.this, "Vui lòng nhập giá sản phẩm", Toast.LENGTH_LONG).show();
                    return;
                }
                String name = bd.edtTenSP.getText().toString();
                String price = bd.edtGia.getText().toString();
                String description = bd.edtDescription.getText().toString();
                StorageReference storageRef = storage.getReference();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bd.imgProduct.setDrawingCacheEnabled(true);
                bd.imgProduct.buildDrawingCache();
                Bitmap bitmap = bd.imgProduct.getDrawingCache();

                String key = UUID.randomUUID().toString();
                String keyProduct = UUID.randomUUID().toString();
                StorageReference imageChild = storageRef.child(key);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = imageChild.putBytes(data);
                image = key;
                Date curDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dateFormat.format(curDate);
                Product p = new Product(keyProduct, name, key, price, description, formattedDateTime);
                firebaseDatabase.child("Product").child(keyProduct).setValue(p);
                Toast.makeText(AddProduct.this, "Thêm thành công", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
    }

    private void setControl() {
        setSupportActionBar(bd.toolbarmngproduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            bd.imgProduct.setImageURI(uri);
        }
    }
}