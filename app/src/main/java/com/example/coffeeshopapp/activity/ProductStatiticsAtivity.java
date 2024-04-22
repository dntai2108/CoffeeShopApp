package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.coffeeshopapp.Adapter.CartAdapter;
import com.example.coffeeshopapp.DAO.CartDAO;
import com.example.coffeeshopapp.DAO.OrderDAO;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.CartDetail;
import com.example.coffeeshopapp.model.Product;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProductStatiticsAtivity extends AppCompatActivity {

    ListView lvproduct_cart_order;
    ImageButton btnBack;
    CartDAO cartdao;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_statitics_ativity);
        lvproduct_cart_order = findViewById(R.id.lvsanpham);
        btnBack = findViewById(R.id.btnBack);
        cartdao = new CartDAO(this);

        Set_btnBack();


//        CartDetail cartdt1 = new CartDetail(1,100,2,1);
//        cartdao.AddCartDetail(cartdt7);
//        Cart cart1 = new Cart(1,1);
//        cartdao.AddCart(cart5);
//        Product product1 = new Product(100, "Cà phê sữa đá","https://cafebuivan.com.vn/thumbs/500x470x2/upload/product/ca-phe-sua-da-7330.png",20000.0);
//        cartdao.AddProduct(product1);
        Set_Data();
//        OrderDAO orderdao = new OrderDAO(this);
//        orderdao.deleteTableCartDetail();
//        orderdao.CreateTable();
//        orderdao.UpdateTable();
    }

    private void Set_Data() {
//        LinkedHashMap<Product,Integer> product_order_list = cartdao.GetTotalOrdersByProduct();
//        CartAdapter cartadapter = new CartAdapter(ProductStatiticsAtivity.this,product_order_list);
//        lvproduct_cart_order.setAdapter(cartadapter);
    }

    private void Set_btnBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}