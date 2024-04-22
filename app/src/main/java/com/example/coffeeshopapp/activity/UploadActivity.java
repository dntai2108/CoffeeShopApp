package com.example.coffeeshopapp.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.DataClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    private FloatingActionButton uploadButton;
    private ImageView uploadImageView;
    private ProgressBar progressBar;
    private Uri imageuri;
    final private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Images");
    final private StorageReference storageReference= FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadButton=findViewById(R.id.uploadButton);
        uploadImageView=findViewById(R.id.uploadImage);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(ImageView.INVISIBLE);

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode()== Activity.RESULT_OK){
                            Intent data=o.getData();
                            imageuri=data.getData();
                            uploadImageView.setImageURI(imageuri);
                        }
                        else {
                            Toast.makeText(UploadActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker= new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageuri!=null){
                    uploadToFirebase(imageuri);
                }else {
                    Toast.makeText(UploadActivity.this, "Plese select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void uploadToFirebase(Uri uri){
        final StorageReference imagereference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        imagereference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Uri của hình ảnh đã tải lên

                        DataClass product=new DataClass(uri.toString());
                        // Thêm đối tượng Product vào cơ sở dữ liệu
                        String key=databaseReference.push().getKey();
                        databaseReference.child(key).setValue(product);
                        // Hiển thị thông báo thành công
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(UploadActivity.this, "uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(UploadActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}