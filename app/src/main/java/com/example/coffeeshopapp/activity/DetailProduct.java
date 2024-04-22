package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityDetailProductAdminBinding;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class DetailProduct extends AppCompatActivity {
    public interface YesNoDialogListener {
        void onYesClicked();
        void onNoClicked();
    }
    private ActivityDetailProductAdminBinding bd;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_admin);
        bd = ActivityDetailProductAdminBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setEvent();
    }

    private void setEvent() {
        StorageReference storageRef = storage.getReference();
        Product product = (Product) getIntent().getSerializableExtra("item");
        bd.edtProductName.setText(product.getName().toString());
        bd.edtProductPrice.setText(product.getPrice().toString());
        bd.edtDescription.setText(product.getDescription().toString());
        if(product.getSize().toString().equals("L")){
            bd.radioButtonSizeL.setChecked(true);
        }
        else{
            bd.radioButtonSizeM.setChecked(true);
        }
        storageRef.child(product.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(bd.imgProduct);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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

        bd.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bd.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYesNoDialog(new YesNoDialogListener() {
                    @Override
                    public void onYesClicked() {
                        StorageReference desertRef = storageRef.child(product.getImage());
                        Drawable drawable = bd.imgProduct.getDrawable();
                        if(drawable != null){
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child("Product").child(product.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DetailProduct.this,"Xóa thành công", Toast.LENGTH_LONG).show();
                                            onBackPressed();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(DetailProduct.this,"Có lỗi khi xóa", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else{
                            Toast.makeText(DetailProduct.this,"Xóa ảnh thành công", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onNoClicked() {
                        return;
                    }
                });
            }
        });
        bd.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = bd.imgProduct.getDrawable();
                if(drawable == null){
                    Toast.makeText(DetailProduct.this,"Hình ảnh không thể trống", Toast.LENGTH_LONG).show();
                    return;
                }
                if(bd.edtProductName.getText().toString().trim().isEmpty()){
                    Toast.makeText(DetailProduct.this,"Tên sản phẩm không thể trống", Toast.LENGTH_LONG).show();
                    return;
                }
                if(bd.edtProductPrice.getText().toString().trim().isEmpty()){
                    Toast.makeText(DetailProduct.this,"Giá không thể trống", Toast.LENGTH_LONG).show();
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bd.imgProduct.setDrawingCacheEnabled(true);
                bd.imgProduct.buildDrawingCache();
                Bitmap bitmap = bd.imgProduct.getDrawingCache();
                String name = bd.edtProductName.getText().toString().replaceAll(" ", "");
                String imgOld = product.getImage().toString();
                String key = UUID.randomUUID().toString();
                StorageReference imageChild = storageRef.child(key);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                StorageReference desertRef = storageRef.child(imgOld);
                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        product.setImage(key);
                        product.setName(bd.edtProductName.getText().toString());
                        product.setPrice(Double.parseDouble(bd.edtProductPrice.getText().toString()));
                        product.setDescription(bd.edtDescription.getText().toString());
                        if(bd.radioButtonSizeM.isChecked()){
                            product.setSize("M");
                        }
                        else{
                            product.setSize("L");
                        }
                        UploadTask uploadTask = imageChild.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailProduct.this,"Có lỗi khi đẩy ảnh lên server", Toast.LENGTH_LONG).show();
                                return;
                            }
                        });

                        databaseReference.child("Product").child(product.getId()).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(DetailProduct.this,"Sửa thành công", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DetailProduct.this,"Có lỗi khi Sửa", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            bd.imgProduct.setImageURI(uri);
        }

    }

    private void showYesNoDialog(final YesNoDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn tiếp tục không?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onYesClicked();
                        dialog.dismiss(); // Đóng hộp thoại
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNoClicked();
                        dialog.dismiss(); // Đóng hộp thoại
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}