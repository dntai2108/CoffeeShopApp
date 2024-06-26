package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffeeshopapp.databinding.ActivityDetailProductBinding;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailProductActivity extends AppCompatActivity {
    // Khai báo biến để lưu số lượng sản phẩm
    private int quantity = 1;
    // Khai báo tham chiếu đến node hoặc bảng trong Firebase Realtime Database
    private DatabaseReference cartRef;
    private DatabaseReference customerRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    private Context context;
    private ActivityDetailProductBinding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        // Khởi tạo context bằng this
        context = this;
        // Khởi tạo tham chiếu đến "Customers" node trong Firebase Realtime Database
        customerRef = FirebaseDatabase.getInstance().getReference("Customer");
        //Lấy dữ liệu của product
        Product product = getIntent().getParcelableExtra("product");
        StorageReference storageRef = storage.getReference();
        storageRef.child(product.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri.toString()).into(bd.imgctsp);
            }
        });
        bd.tvNameItem.setText(product.getName());
        bd.tvPriceItemfl.setText(product.getPrice());
        setEvent();

    }// ngoài onCreate

    private void setEvent() {
        bd.backtolistproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bd.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng số lượng sản phẩm lên 1 đơn vị
                quantity++;
                // Cập nhật số lượng sản phẩm hiển thị trên giao diện
                updateQuantityTextView();
            }
        });
        bd.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra số lượng sản phẩm không được nhỏ hơn 1
                if (quantity > 1) {
                    // Giảm số lượng sản phẩm đi 1 đơn vị
                    quantity--;
                    // Cập nhật số lượng sản phẩm hiển thị trên giao diện
                    updateQuantityTextView();
                }
            }
        });
        bd.radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                String selectedSize = radioButton.getText().toString();

                Toast.makeText(DetailProductActivity.this, "Selected size: " + selectedSize, Toast.LENGTH_SHORT).show();

            }
        });
        bd.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Lấy thông tin sản phẩm từ Intent
                Product product = getIntent().getParcelableExtra("product");

                // Lấy mã khách hàng của khách hàng hiện tại, nếu không có thì tạo mã mới
                String customerId = getCustomerId();// hàm viết ở dưới
                // Lấy kích thước được chọn
                String selectedSize = "";

                int selectedRadioButtonId = bd.radgroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {
                    RadioButton radioButton = findViewById(selectedRadioButtonId);
                    selectedSize = radioButton.getText().toString();
                }
                // Tạo một đối tượng Cart đại diện cho sản phẩm được chọn
                Cart cartItem = new Cart(product, String.valueOf(quantity), selectedSize); // Cập nhật thông tin kích thước
                // Gửi thông tin giỏ hàng của khách hàng lên Firebase
                sendCartDataToFirebase(customerId, cartItem);
            }
        });
    }// ngoài setEvent()

    private String getCustomerId() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        return userId;
    }

    private void sendCartDataToFirebase(String customerId, Cart cartItem) {

        // Set giá trị của cartItem lên Firebase với key là ID của sản phẩm
        DatabaseReference cartItemRef = customerRef.child(customerId).child("Cart").child(cartItem.getProduct().getId());
        cartItemRef.child("quantity").setValue(cartItem.getQuantity()); // Lưu số lượng
        cartItemRef.child("size").setValue(cartItem.getSize()); // Lưu kích thước

        // Gửi dữ liệu và xử lý kết quả (nếu cần)
        cartItemRef.setValue(cartItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Gửi dữ liệu thành công
                            Toast.makeText(DetailProductActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xảy ra lỗi khi gửi dữ liệu
                            Toast.makeText(DetailProductActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Phương thức cập nhật số lượng sản phẩm hiển thị trên giao diện
    private void updateQuantityTextView() {
        bd.tvQuantityofProduct.setText(String.valueOf(quantity));
    }


}