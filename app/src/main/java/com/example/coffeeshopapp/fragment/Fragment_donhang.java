package com.example.coffeeshopapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.Adapter.RecyclerViewDonHangAdapter;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.Chitiet_donhang_dadat_activity;
import com.example.coffeeshopapp.model.Order;
import com.example.coffeeshopapp.model.Productimgurl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_donhang#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_donhang extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference("Customer").child("Customer123").child("Order");


    ArrayList<Order> orderList;
    RecyclerView recyclerView;
    RecyclerViewDonHangAdapter recyclerViewDonHangAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_donhang() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_donhang.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_donhang newInstance(String param1, String param2) {
        Fragment_donhang fragment = new Fragment_donhang();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donhang, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerviewDonHang);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        orderList = new ArrayList<>();
        recyclerViewDonHangAdapter = new RecyclerViewDonHangAdapter(orderList, getContext());
        recyclerView.setAdapter(recyclerViewDonHangAdapter);
        setEVent();
    }

    private void setEVent() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String time = dataSnapshot.child("orderDate").getValue(String.class);
                        String madonhang = dataSnapshot.child("orderId").getValue(String.class);
                        String status = dataSnapshot.child("status").getValue(String.class);
                        String tongtien = dataSnapshot.child("totalAmount").getValue(String.class);
//                        Order o= snapshot.getValue(Order.class)
                        orderList.add(new Order(madonhang, time, status, tongtien));
                    }
                    recyclerViewDonHangAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}