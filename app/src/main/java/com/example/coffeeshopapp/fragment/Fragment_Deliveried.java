package com.example.coffeeshopapp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.RecycleViewFragmentDeliveried;
import com.example.coffeeshopapp.adapter.RecycleViewFragmentDelivering;
import com.example.coffeeshopapp.databinding.FragmentDeliveriedBinding;
import com.example.coffeeshopapp.databinding.FragmentDeliveringBinding;
import com.example.coffeeshopapp.model.Customer;
import com.example.coffeeshopapp.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Deliveried#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Deliveried extends Fragment {

    private FragmentDeliveriedBinding bd;

    private ArrayList<Order> orderList;
    private RecycleViewFragmentDeliveried recycleViewFragmentDeliveried;

    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Deliveried() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Deliveried.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Deliveried newInstance(String param1, String param2) {
        Fragment_Deliveried fragment = new Fragment_Deliveried();
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
        bd = FragmentDeliveriedBinding.inflate(getLayoutInflater());
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEvent();
    }

    private void setEvent() {
        this.orderList = new ArrayList<>();
        reloadOrder();
        bd.recyclervieworder.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recycleViewFragmentDeliveried = new RecycleViewFragmentDeliveried(orderList,getContext());
        bd.recyclervieworder.setAdapter(recycleViewFragmentDeliveried);
    }
    @Override
    public void onResume() {
        super.onResume();
        reloadOrder();
    }
    public void reloadOrder(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: dataSnapshot.child("Order").getChildren()){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Order o = new Order();
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderId = orderSnapshot.child("orderId").getValue(String.class);
                        String status = orderSnapshot.child("status").getValue(String.class);
                        String totalAmount = orderSnapshot.child("totalAmount").getValue(String.class);
                        String shipperId = orderSnapshot.child("shipperId").getValue(String.class);
                        String customerId = dataSnapshot.child("id").getValue(String.class);
                        o.setCustomerId(customerId);
                        o.setOrderId(orderId);
                        o.setOrderDate(orderDate);
                        o.setStatus(status);
                        o.setTotalAmount(totalAmount);
                        o.setShipperID(shipperId);
                        if(o.getStatus().equalsIgnoreCase("Đang giao") && o.getShipperID().equals(userId)){
                            orderList.add(o);
                        }
                    }
                    recycleViewFragmentDeliveried.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}