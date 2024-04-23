package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Product;

import java.util.LinkedHashMap;

public class CartAdapter extends ArrayAdapter<Product> {

    private Context context;
    private LinkedHashMap<Product, Integer> productList;

    public CartAdapter(Context context, LinkedHashMap<Product, Integer> productList) {
        super(context, 0);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_cart, parent, false);
        }

        Product product = (Product) productList.keySet().toArray()[position];
        int quantity = productList.get(product);

        // Ánh xạ các view trong layout_item.xml
        TextView tvstt = convertView.findViewById(R.id.stt);
        ImageView imgproduct = convertView.findViewById(R.id.img);
        TextView tvname = convertView.findViewById(R.id.name);
        TextView tvprice = convertView.findViewById(R.id.price);
        TextView tvnumber_order = convertView.findViewById(R.id.quatity);

        // Hiển thị thông tin của Product lên các TextView tương ứng
        tvstt.setText(String.valueOf(position + 1));
        String imgUrl = product.getImage();
        Glide.with(context).load(imgUrl).into(imgproduct);
        tvname.setText(product.getName());
        tvprice.setText(String.valueOf(product.getPrice()));
        tvnumber_order.setText(String.valueOf(quantity));

        return convertView;
    }

    @Override
    public int getCount() {
        return productList.size();
    }
}
