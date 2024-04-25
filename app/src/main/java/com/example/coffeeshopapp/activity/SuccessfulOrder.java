package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.coffeeshopapp.databinding.ActivitySuccessfulOrderBinding;
import com.example.coffeeshopapp.fragment.Fragment_trangchu;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Customer;
import com.example.coffeeshopapp.model.Order;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class SuccessfulOrder extends AppCompatActivity {
    private ActivitySuccessfulOrderBinding bd;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivitySuccessfulOrderBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setEvent();
    }

    private void setEvent() {

        // Tạo đơn hàng từ thông tin trong giỏ hàng
        createOrderFromCart();

        // Xóa các sản phẩm trong giỏ hàng sau khi đặt hàng thành công
        clearCart();

        // Trở về danh sách sản phẩm
        bd.btnTieptucmuasam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessfulOrder.this, Bottom_nav.class);
                startActivity(intent);
            }
        });
    }// đóng setEvent()

    private void clearCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        databaseReference.child("Customer").child(userId).child("Cart").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SuccessfulOrder.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SuccessfulOrder.this, "Error clearing cart: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createOrderFromCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        databaseReference.child("Customer").child(userId).child("Cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Cart> cartList = new ArrayList<>();
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product product = productSnapshot.child("product").getValue(Product.class);
                        String quantity = productSnapshot.child("quantity").getValue(String.class);
                        String size = productSnapshot.child("size").getValue(String.class);
                        Cart cart = new Cart(product, quantity, size);
                        cartList.add(cart);
                    }
                    createOrder(cartList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
                Toast.makeText(SuccessfulOrder.this, "Error reading data from Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder(List<Cart> cartList) {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        Date currentTime = new Date();
        // Định dạng ngày giờ
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Chuyển đổi đối tượng Date thành chuỗi định dạng
        String formattedDateTime = dateFormat.format(currentTime);
        double totalAmount = calculateTotalAmount(cartList);


        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference()
                .child("Customer").child(userId);
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy thông tin của khách hàng từ nút "Info"
                    String customerName = snapshot.child("name").getValue(String.class);
                    String customerPhone = snapshot.child("phone").getValue(String.class);
                    String customerAddress = snapshot.child("address").getValue(String.class);

                    // Tạo một đối tượng Customer với thông tin từ Firebase
                    Customer customer = new Customer(customerName, customerAddress, customerPhone);
                    // sinh mã tự động
                    String keyorder = UUID.randomUUID().toString().substring(0, 10);

                    // Tạo một đơn hàng mới
                    Order order = new Order();
                    order.setStatus("Chờ duyệt");
                    order.setOrderDate(formattedDateTime);
                    DecimalFormat decimalFormatS = new DecimalFormat("###,###,###");
                    String tongtien = decimalFormatS.format(totalAmount) + " vnd";
                    order.setTotalAmount(tongtien);
                    order.setCustomer(customer); // Thiết lập đối tượng Customer
                    order.setCartList(cartList);
                    order.setOrderId(keyorder);

                    // Ghi đơn hàng lên Firebase
                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Customer")
                            .child(userId).child("Order").child(keyorder);
                    orderRef.setValue(order);
                } else {
                    Toast.makeText(SuccessfulOrder.this, "No customer address found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data from Firebase: " + error.getMessage());
                Toast.makeText(SuccessfulOrder.this, "Error reading data from Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateTotalAmount(List<Cart> cartList) {
        double totalAmount = 0;
        for (Cart cartItem : cartList) {
            Double cartItemPrice = Double.parseDouble(cartItem.getProduct().getPrice().replace(".", ""));
            int cartItemQuantity = Integer.parseInt(cartItem.getQuantity());

            totalAmount += cartItemPrice * cartItemQuantity;
        }
        return totalAmount;
    }
}