package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.coffeeshopapp.Adapter.RecycleViewManageUserAdapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityManageUserBinding;
import com.example.coffeeshopapp.model.Customer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageUser extends AppCompatActivity {
    private ActivityManageUserBinding bd;
    private ArrayList<Customer> listCustomers;
    private RecycleViewManageUserAdapter recycleViewManageUserAdapter;
    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityManageUserBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_manage_user);
        setContentView(bd.getRoot());
        setData();
        setControl();
        setEven();
    }

    private void setEven() {
        reloadCustomers();
        bd.svUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                databaseReferences.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listCustomers.clear();
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Customer customer = dataSnapshot.getValue(Customer.class);
                            if(customer.getName().toLowerCase().contains(newText.toLowerCase())){
                                listCustomers.add(customer);
                            }
                        }
                        recycleViewManageUserAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }
        });
    }

    private void setControl() {
        bd.toolbarmnguser.setTitle("KHÁCH HÀNG");
        setSupportActionBar(bd.toolbarmnguser);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setData() {
        this.listCustomers = new ArrayList<>();
        reloadCustomers();
        bd.RecyclerViewUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycleViewManageUserAdapter = new RecycleViewManageUserAdapter(listCustomers,this);
        bd.RecyclerViewUser.setAdapter(recycleViewManageUserAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        reloadCustomers();
    }
    public void reloadCustomers (){
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCustomers.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Customer customer = dataSnapshot.getValue(Customer.class);
                    listCustomers.add(customer);
                }
                recycleViewManageUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}