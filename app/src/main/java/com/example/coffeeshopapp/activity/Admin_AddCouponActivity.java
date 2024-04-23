package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffeeshopapp.databinding.ActivityCouponBinding;
import com.example.coffeeshopapp.model.Coupon;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.UUID;

public class Admin_AddCouponActivity extends AppCompatActivity {
    private ActivityCouponBinding bd;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coupon");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=ActivityCouponBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setEvent();
    }

    private void setEvent() {
        bd.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bd.btnNgaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bd.edtNgaybatdau);
            }
        });
        bd.btnNgaykethuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bd.edtNgayketthuc);
            }
        });
        bd.btnThemmagiamgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sinh mã tự động
                String keyorder = UUID.randomUUID().toString().substring(0, 10);
                String magiamgia = bd.edtmagiamgia.getText().toString();
                String ngayBD = bd.edtNgaybatdau.getText().toString();
                String ngayKT = bd.edtNgayketthuc.getText().toString();
                String phantramgiam = bd.edtphantramgiam.getText().toString()+" %";
                Coupon coupon= new Coupon(keyorder,magiamgia,phantramgiam,ngayBD,ngayKT);

                try {
                    databaseReference.child(keyorder).setValue(coupon, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                // Xảy ra lỗi khi thực hiện thêm dữ liệu
                                Log.e("AddCouponActivity", "Error adding coupon to database: " + error.getMessage());
                                Toast.makeText(Admin_AddCouponActivity.this, "Thêm mã giảm giá thất bại", Toast.LENGTH_SHORT).show();
                            } else {
                                // Thêm dữ liệu thành công
                                Log.d("AddCouponActivity", "Coupon added successfully");
                                Toast.makeText(Admin_AddCouponActivity.this, "Thêm mã giảm giá thành công", Toast.LENGTH_SHORT).show();
                                 bd.edtmagiamgia.setText("");
                                 bd.edtNgaybatdau.setText("");
                                 bd.edtNgayketthuc.setText("");
                                 bd.edtphantramgiam.setText("");
                            }
                        }
                    });
                } catch(Exception e) {
                    // Xảy ra lỗi không mong muốn
                    Log.e("AddCouponActivity", "Unexpected error: " + e.getMessage(), e);
                    Toast.makeText(Admin_AddCouponActivity.this, "Đã xảy ra lỗi không mong muốn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }// end setevent
    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Admin_AddCouponActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}