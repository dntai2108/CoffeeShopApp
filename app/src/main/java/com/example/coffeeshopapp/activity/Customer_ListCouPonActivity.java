package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.Admin_RecyclerViewListCouponAdapter;
import com.example.coffeeshopapp.adapter.Customer_RecyclerViewListCouPonAdapter;
import com.example.coffeeshopapp.databinding.ActivityCustomerListCouPonBinding;
import com.example.coffeeshopapp.model.Coupon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Customer_ListCouPonActivity extends AppCompatActivity implements Customer_RecyclerViewListCouPonAdapter.OnCouponSelectedListener{
    private ActivityCustomerListCouPonBinding bd;
    ArrayList<Coupon> couponArrayList;
    Customer_RecyclerViewListCouPonAdapter couponAdapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coupon");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd=ActivityCustomerListCouPonBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        bd.recyclerViewListCoupon.setHasFixedSize(true);
        bd.recyclerViewListCoupon.setLayoutManager(new LinearLayoutManager(this));
        couponArrayList = new ArrayList<>();
        couponAdapter = new Customer_RecyclerViewListCouPonAdapter(Customer_ListCouPonActivity.this, couponArrayList,this);
        bd.recyclerViewListCoupon.setAdapter(couponAdapter);

        setEvent();
    }

    private void setEvent() {
        bd.btnBack.setOnClickListener(v -> finish());

        //Lấy list mã giảm giá
        // Lấy ngày hôm nay
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                couponArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Coupon coupon = dataSnapshot.getValue(Coupon.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        assert coupon != null;
                        Date endDate = sdf.parse(coupon.getNgayKetThuc());

                        if (endDate != null && endDate.compareTo(today) >= 0) {
                            // Nếu ngày kết thúc của mã giảm giá lớn hơn hoặc bằng ngày hiện tại, thêm mã đó vào danh sách hiển thị
                            couponArrayList.add(coupon);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                couponAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onCouponSelected(Coupon coupon) {
        String couponCode = coupon.getTenMa();
        String discountPercent = coupon.getPhanTramGiam();

        // Tạo Intent và đính kèm dữ liệu
        Intent intent = new Intent(this, CartActivity.class);
        intent.putExtra("COUPON_CODE", couponCode);
        intent.putExtra("DISCOUNT_PERCENT", discountPercent);

        // Chuyển sang CartActivity
        startActivity(intent);
    }
}