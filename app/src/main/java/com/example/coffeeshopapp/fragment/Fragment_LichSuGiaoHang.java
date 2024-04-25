package com.example.coffeeshopapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.RecycleViewManageOrderAdapter;
import com.example.coffeeshopapp.databinding.FragmentLichSuGiaoHangBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_LichSuGiaoHang#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_LichSuGiaoHang extends Fragment {
    private FragmentLichSuGiaoHangBinding bd;
    private ArrayList<Order> orderList;
    private RecycleViewManageOrderAdapter recycleViewManageOrderAdapter;
    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_LichSuGiaoHang() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_LichSuGiaoHang.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_LichSuGiaoHang newInstance(String param1, String param2) {
        Fragment_LichSuGiaoHang fragment = new Fragment_LichSuGiaoHang();
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
        bd = FragmentLichSuGiaoHangBinding.inflate(getLayoutInflater());
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();
        setControl();
        setEven();
    }

    private void setEven() {
    }

    private void setControl() {
        bd.toolbarmnguser.setTitle("LỊCH SỬ GIAO HÀNG");
    }

    private void setData() {
        String[] items = {"Tất cả","Hoàn thành", "Đã hủy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bd.SpinnerOder.setAdapter(adapter);
        bd.SpinnerOder.setOnItemSelectedListener(spinnerListener);
        this.orderList = new ArrayList<>();
        reloadOrder();
        bd.RecyclerViewOrder.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recycleViewManageOrderAdapter = new RecycleViewManageOrderAdapter(orderList,getContext());
        bd.RecyclerViewOrder.setAdapter(recycleViewManageOrderAdapter);
    }

    public void reloadOrder(){
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
                        String customerId = dataSnapshot.getKey();
                        o.setCustomerId(customerId);
                        o.setOrderId(orderId);
                        o.setOrderDate(orderDate);
                        o.setStatus(status);
                        o.setTotalAmount(totalAmount);
                        if(status.equals("Hoàn thành") || status.equals("Đã hủy")){
                            orderList.add(o);
                        }
                    }
                }
                recycleViewManageOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void reloadOrderWithCondition(String trangThai){
        databaseReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    for(DataSnapshot orderSnapshot: dataSnapshot.child("Order").getChildren()){
                        Order o = new Order();
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderId = orderSnapshot.child("orderId").getValue(String.class);
                        String status = orderSnapshot.child("status").getValue(String.class);
                        String totalAmount = orderSnapshot.child("totalAmount").getValue(String.class);
                        String customerId = dataSnapshot.getKey();
                        o.setCustomerId(customerId);
                        o.setOrderId(orderId);
                        o.setOrderDate(orderDate);
                        o.setStatus(status);
                        o.setTotalAmount(totalAmount);
                        if(status.equals(trangThai)){
                            orderList.add(o);
                        }
                    }
                }
                recycleViewManageOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();
            if(selectedItem.equals("Tất cả")){
                reloadOrder();
            }
            else if(selectedItem.equals("Hoàn thành")){
                reloadOrderWithCondition("Hoàn thành");
            }
            else if(selectedItem.equals("Đã hủy")){
                reloadOrderWithCondition("Đã hủy");
            }
            recycleViewManageOrderAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}