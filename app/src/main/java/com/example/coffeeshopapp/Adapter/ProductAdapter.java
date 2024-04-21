package com.example.coffeeshopapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.DetailProduct;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context context;
    private int resource;
    private List<Product> data;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        ImageView ivImage = convertView.findViewById(R.id.ivProductImage);
        TextView tvName = convertView.findViewById(R.id.tvProductName);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdateProduct);
        Product product = data.get(position);
        StorageReference storageRef = storage.getReference();
        storageRef.child(product.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(ivImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String a = e.getMessage();
                Log.e("ImageDownload", "Error downloading image: " + e.getMessage());
            }
        });
        tvName.setText(product.getName());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, DetailProduct.class);
                intent.putExtra("item",product);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
