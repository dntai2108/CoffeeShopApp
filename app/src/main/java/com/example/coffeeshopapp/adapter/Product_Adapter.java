package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Product;


import java.util.List;

public class Product_Adapter extends ArrayAdapter<Product> {
    private Context mContext;
    private List<Product> mProductList;


    public Product_Adapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        mContext = context;
        mProductList = productList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.layou_item_product, parent, false);
        }

        // Lấy sản phẩm tại vị trí hiện tại trong danh sách
        Product currentProduct = mProductList.get(position);

        // Ánh xạ các thành phần giao diện
        ImageView imageView = listItemView.findViewById(R.id.img_product);
        TextView nameTextView = listItemView.findViewById(R.id.txtname);

        // Đặt tên sản phẩm
        nameTextView.setText(currentProduct.getName());

        // Tải và hiển thị hình ảnh từ đường dẫn URL
        Glide.with(mContext)
                .load(currentProduct.getImage())
                .apply(new RequestOptions())
                .into(imageView);

        return listItemView;
    }
}

