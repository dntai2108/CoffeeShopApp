package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityCartBinding;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.adapter.CartItemAdapter;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnDeleteItemClickListener, CartItemAdapter.OnQuantityChangeListener {
    private RecyclerView recyclerView;
    private CartItemAdapter cartItemAdapter;
    private List<Cart> cartItemList;
    private ActivityCartBinding bd;

    private static final int REQUEST_CHANGE_ADDRESS = 1;
    private static final String magiamgia = "CAFE";
    private static final Double phivanchuyen = 10000.0;
    private static final Double giam5phantram = 0.95;// giảm 5%
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("Customer").child("Customer123").child("Cart");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewlistcartproduct);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        cartItemList = new ArrayList<>();
        cartItemAdapter = new CartItemAdapter(cartItemList, this);
        recyclerView.setAdapter(cartItemAdapter);


        fetchDataFromFirebase();
        layThongTinDiaChi();
        cartItemAdapter.setOnDeleteItemClickListener(this);
        cartItemAdapter.setOnQuantityChangeListener(this);
        setEvent();

    }// ngoài onCreate()

    // Nhận thng tin cập nhật địa chỉ
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String newAddress = data.getStringExtra("newAddress");
                // Hiển thị địa chỉ mới trên màn hình giỏ hàng
                bd.tvAddressfc.setText(newAddress);
            }
        }
    }

    private void setEvent() {
        // nút back
        bd.imgbackincart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        // nút chọn mã giảm giá
        bd.btnSelectCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CartActivity.this,Customer_ListCouPonActivity.class);
                startActivity(intent);
            }
        });
        // nút áp mã giảm giá
        bd.btnCouPon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponCode = bd.edtCoupon.getText().toString().trim();
                if (couponCode.isEmpty()) {
                    // Kiểm tra xem mã giảm giá có được nhập hay không
                    Toast.makeText(CartActivity.this, "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra xem mã giảm giá có đúng không
                    if (couponCode.equals(magiamgia)) {
                        // Áp dụng mã giảm giá
                        double totalPrice = Double.parseDouble(bd.tvpricetotalofcart.getText().toString()
                                .replace(" vnd", "").replace(",", ""));
                        double discountedPrice = totalPrice * giam5phantram;
                        displayTotalPrice(discountedPrice);
                        Toast.makeText(CartActivity.this, "Áp dụng mã giảm giá thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mã giảm giá không đúng
                        Toast.makeText(CartActivity.this, "Mã giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // nút đặt hàng bằng tiền mặt
        bd.btnPayofcartTienmat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, SuccessfulOrder.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        // nút thay đổi địa chỉ
        bd.btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, UpdateAddress.class);
                startActivityForResult(intent, REQUEST_CHANGE_ADDRESS);
            }
        });
    }

    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Cart cartItem : cartItemList) {
            // Tính giá tiền cho từng sản phẩm và cộng vào tổng giá tiền
            //String "100.000" -> 100000.0 rồi lấy giá nhân số lượng
            double cartItemPrice = Double.parseDouble(cartItem.getProduct().getPrice().replace(".", ""));
            int cartItemQuantity = Integer.parseInt(cartItem.getQuantity());

            totalPrice += cartItemPrice * cartItemQuantity;
        }
        // Hiển thị tổng giá tiền trên giao diện
        displayTotalPrice(totalPrice);
    }

    // Tạo một định dạng số
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @SuppressLint("SetTextI18n")
    private void displayTotalPrice(double totalPrice) {
        // Chuyển đổi tổng giá tiền sang định dạng số tiền Việt Nam
        String formattedPrice = decimalFormat.format(totalPrice);
        // Hiển thị tổng giá tiền trên giao diện khi chưa có phí vận chuyển
        bd.tvpriceofcart.setText(formattedPrice + " vnd");
        // tổng tiền khi cộng thêm phí vận chuyển
        String formattedPricetotal = decimalFormat.format(totalPrice + phivanchuyen);
        bd.tvpricetotalofcart.setText(formattedPricetotal + " vnd");
    }

    //Lấy dữ liệu các sản phẩm trong giỏ hàng từ Firebase
    private void fetchDataFromFirebase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                // Kiểm tra xem nếu có dữ liệu trong giỏ hàng của khách hàng
                if (snapshot.exists()) {
                    // Duyệt qua tất cả các nút con (các sản phẩm) trong giỏ hàng
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        // Lấy thông tin về sản phẩm
                        Product product = productSnapshot.child("product").getValue(Product.class);
                        String quantity = productSnapshot.child("quantity").getValue(String.class);
                        String size = productSnapshot.child("size").getValue(String.class);
                        Cart cart = new Cart(product, quantity, size);
                        cartItemList.add(cart);
                    }
                    cartItemAdapter.notifyDataSetChanged();
                    calculateTotalPrice();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hiển thị thông báo lỗi
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage());
                Toast.makeText(CartActivity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }// end fetch data from firebase

    private void layThongTinDiaChi() {
        // Lấy thông tin địa chỉ
        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference("Customer")
                .child("Customer123").child("Info");
        customerInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem có dữ liệu tồn tại không
                if (snapshot.exists()) {
                    // Lấy địa chỉ từ dữ liệu
                    String address = snapshot.child("address").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    //Set data cho textview
                    bd.tvAddressfc.setText(address);
                    bd.tvNameCusfc.setText(name);
                    bd.tvphonefc.setText(phone);
                } else {
                    bd.tvAddressfc.setText("Rỗng");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(CartActivity.this, "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @Override
    public void onDeleteItemClick(int position) {
        Cart deleteItem = cartItemList.get(position);
        String productId = deleteItem.getProduct().getId(); // Lấy id sản phẩm
        //  final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
        //            .getReference("Customer").child("Customer123").child("Cart");
        DatabaseReference itemRef = databaseReference.child(productId);
        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Xoá thành công từ Firebase, cập nhật lại danh sách và tính lại tổng giá tiền
                    cartItemList.remove(position);
                    cartItemAdapter.notifyItemRemoved(position);
                    calculateTotalPrice();
                    Toast.makeText(CartActivity.this, "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi xoá từ Firebase
                    Toast.makeText(CartActivity.this, "Lỗi khi xoá sản phẩm: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Tăng số lượng sản phẩm
    @Override
    public void onIncreaseQuantity(int position) {
        Cart cartItem = cartItemList.get(position);
        int currentQuantity = Integer.parseInt(cartItem.getQuantity());
        currentQuantity++;
        cartItem.setQuantity(String.valueOf(currentQuantity));

        // Cập nhật số lượng sản phẩm trong Firebase
        DatabaseReference itemRef = databaseReference.child(cartItem.getProduct().getId()); // Giả sử có một trường id trong đối tượng Cart để định danh mỗi sản phẩm
        itemRef.child("quantity").setValue(String.valueOf(currentQuantity));

        // Cập nhật lại giao diện
        cartItemAdapter.notifyItemChanged(position);
        calculateTotalPrice();
    }

    // Giảm số lượng sản phẩm
    @Override
    public void onDecreaseQuantity(int position) {
        Cart cartItem = cartItemList.get(position);
        int currentQuantity = Integer.parseInt(cartItem.getQuantity());
        if (currentQuantity > 1) {
            currentQuantity--;
            cartItem.setQuantity(String.valueOf(currentQuantity));

            // Cập nhật số lượng sản phẩm trong Firebase bằng id
            DatabaseReference itemRef = databaseReference.child(cartItem.getProduct().getId());
            itemRef.child("quantity").setValue(String.valueOf(currentQuantity));

            // Cập nhật lại giao diện
            cartItemAdapter.notifyItemChanged(position);
            calculateTotalPrice();
        }
    }
}