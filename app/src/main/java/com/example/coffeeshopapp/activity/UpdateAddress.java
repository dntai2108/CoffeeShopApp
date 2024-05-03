package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityUpdateAddressBinding;
import com.example.coffeeshopapp.fragment.Fragment_giohang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateAddress extends AppCompatActivity {
    private ActivityUpdateAddressBinding bd;
    private DatabaseReference customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityUpdateAddressBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        String newAddress = getIntent().getStringExtra("MapToAddress");
        String Latitude = getIntent().getStringExtra("Latitude");
        String Longitude = getIntent().getStringExtra("Longitude");
        customerRef = FirebaseDatabase.getInstance().getReference("Customer")
                .child(userId).child("address");

        if(newAddress!=null && !newAddress.isEmpty()){
            bd.edtAddress.setText(newAddress);
        }
        bd.btnChoseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateAddress.this,PickAdressMap.class);
                if(Longitude!=null && Latitude!=null){
                    intent.putExtra("Longitude",Longitude);
                    intent.putExtra("Latitude",Latitude);
                }
                startActivity(intent);
            }
        });
        bd.btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem người dùng đã nhập địa chỉ mới hay chưa
                if (!newAddress.isEmpty()) {
                    // Cập nhật địa chỉ mới vào Firebase
                    customerRef.setValue(newAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Địa chỉ đã được cập nhật thành công
                                        Toast.makeText(UpdateAddress.this, "Địa chỉ đã được cập nhật", Toast.LENGTH_SHORT).show();
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("newAddress", newAddress);
                                        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("Customer").child(userId);
                                        locationRef.child("Latitude").setValue(Latitude);
                                        locationRef.child("Longitude").setValue(Longitude);
                                        setResult(RESULT_OK, resultIntent);
                                        finish(); // Kết thúc Activity sau khi cập nhậtúc Activity sau khi cập nhật
                                    } else {
                                        // Xảy ra lỗi khi cập nhật địa chỉ
                                        Toast.makeText(UpdateAddress.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    // Hiển thị thông báo nếu người dùng không nhập địa chỉ mới
                    Toast.makeText(UpdateAddress.this, "Vui lòng nhập địa chỉ mới", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}