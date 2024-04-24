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
import com.example.coffeeshopapp.adapter.RecycleViewFragmentDelivering;
import com.example.coffeeshopapp.databinding.FragmentDeliveringBinding;
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
 * Use the {@link Fragment_Delivering#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Delivering extends Fragment {
    private FragmentDeliveringBinding bd;

    private ArrayList<Order> orderList;
    private RecycleViewFragmentDelivering recycleViewFragmentDelivering;

    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Delivering() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Delivering.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Delivering newInstance(String param1, String param2) {
        Fragment_Delivering fragment = new Fragment_Delivering();
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
        bd = FragmentDeliveringBinding.inflate(getLayoutInflater());
        // Inflate the layout for this fragment
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
        bd.RecyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recycleViewFragmentDelivering = new RecycleViewFragmentDelivering(orderList,getContext());
        bd.RecyclerViewOrder.setAdapter(recycleViewFragmentDelivering);
    }

    public void reloadOrder(){
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId", "");
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
                        o.setOrderId(orderId);
                        o.setOrderDate(orderDate);
                        o.setStatus(status);
                        o.setTotalAmount(totalAmount);
                        o.setShipperID(shipperId);
                        if(o.getStatus().equalsIgnoreCase("Chuẩn bị đơn hàng") && o.getShipperID().equals(userId)){
                            orderList.add(o);
                        }
                    }
                    recycleViewFragmentDelivering.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}