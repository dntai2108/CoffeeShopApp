package com.example.coffeeshopapp.activity;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import com.example.coffeeshopapp.Adapter.Myadapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.DataClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;

   // ArrayList<Product> datalist;
    ArrayList<DataClass> datalist;
    Myadapter myadapter;
    final private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Images");
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        fab=findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        myadapter=new Myadapter(datalist, this);
        recyclerView.setAdapter(myadapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()  )
                {
                    DataClass product =dataSnapshot.getValue(DataClass.class);
                    datalist.add(product);

                }
                myadapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,UploadActivity.class);
            startActivity(intent);
            finish();
        });
    }
}