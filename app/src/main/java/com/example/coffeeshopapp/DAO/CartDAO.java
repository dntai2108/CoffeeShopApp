package com.example.coffeeshopapp.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.coffeeshopapp.Database;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.CartDetail;
import com.example.coffeeshopapp.model.Order;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CartDAO {
    private Database dbHelper;
    private SQLiteDatabase database;
    public CartDAO(Context context) {
        dbHelper = new Database(context);
        database = dbHelper.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
    }

    // Thêm Product:
    public void AddProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put("ID", product.getId());
        values.put("NameProduct", product.getName());
        values.put("Image",product.getImage());
        values.put("PriceProduct",product.getPrice());
        database.insert("Product", null, values);
    }
    // Thêm Cart
    public void AddCart(Cart cart) {
        ContentValues values = new ContentValues();
        values.put("ID", cart.getID());
        values.put("Status", cart.getStatus());
        database.insert("Cart", null, values);
    }
    //Thêm CartDetail
    public void AddCartDetail(CartDetail cartDetail) {
        ContentValues values = new ContentValues();
        values.put("IDCart", cartDetail.getId());
        values.put("IDProduct", cartDetail.getIdProduct());
        values.put("Quantity", cartDetail.getQuantity());
        values.put("Size", cartDetail.getSize());
        database.insert("CartDetail", null, values);
    }
    public CompletableFuture<LinkedHashMap<Product, Integer>> GetTotalOrdersByProduct() {
        CompletableFuture<LinkedHashMap<Product, Integer>> future = new CompletableFuture<>();

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Customer");
        LinkedHashMap<Product, Integer> list_product = new LinkedHashMap<>();

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot customerSnapshot : snapshot.getChildren()) { // danh sách khách hàng
                    // customerSnapshot ở đây là customer
                    for (DataSnapshot orderSnapshot : customerSnapshot.child("Orders").getChildren()) { // danh sách order
                        int order_count = 0;
                        for (DataSnapshot productSnapshot : orderSnapshot.child("Cart").child("Product").getChildren()) { // danh sách product
                            Product product = new Product();
                            // Lấy thông tin của product từ node Product
                            product.setId(Integer.parseInt(Objects.requireNonNull(productSnapshot.child("DetailProduct").child("id").getValue(String.class))));
                            product.setName(productSnapshot.child("DetailProduct").child("name").getValue(String.class));
                            product.setImage(productSnapshot.child("DetailProduct").child("image").getValue(String.class));

                            order_count++;
                            if (list_product.containsKey(product)) {
                                order_count += list_product.get(product); // Tăng số lượng của sản phẩm đã có
                            }
                            list_product.put(product, order_count);
                        }
                    }
                }
                future.complete(list_product); // Hoàn tất CompletableFuture với dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(error.toException()); // Xử lý lỗi nếu có
            }
        });

        return future;
    }


//        String[] columns = {"cd.IDProduct", "p.Image", "p.PriceProduct", "p.NameProduct", "COUNT(cd.IDCart) AS TotalOrders"};
//        String selection = "c.Status = ?";
//        String[] selectionArgs = {"1"};
//        String groupBy = "cd.IDProduct, p.NameProduct";
//
//        // Thực hiện phép nối bảng
//        String tables = "CartDetail cd JOIN Product p ON cd.IDProduct = p.ID JOIN Cart c ON cd.IDCart = c.ID";
//
//        Cursor cursor = database.query(
//                tables,
//                columns,
//                selection,
//                selectionArgs,
//                groupBy,
//                null,
//                "TotalOrders DESC"
//        );
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                // Lấy dữ liệu từ cursor và tạo đối tượng Product
//                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("IDProduct"));
//                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex("Image"));
//                @SuppressLint("Range") String nameProduct = cursor.getString(cursor.getColumnIndex("NameProduct"));
//                @SuppressLint("Range") double priceProduct = cursor.getDouble(cursor.getColumnIndex("PriceProduct"));
//                @SuppressLint("Range") int totalOrders = cursor.getInt(cursor.getColumnIndex("TotalOrders"));
//                Product product = new Product(id, nameProduct, image, priceProduct);
//
//                // Thêm đối tượng Product và số đơn hàng vào LinkedHashMap
//                orderlist_by_product.put(product, totalOrders);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }






}
