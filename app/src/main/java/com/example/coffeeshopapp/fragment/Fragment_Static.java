package com.example.coffeeshopapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.adapter.RecyclerViewStatiticAdapter;
import com.example.coffeeshopapp.databinding.FragmentStaticBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Static#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Static extends Fragment {
    private @NonNull FragmentStaticBinding bd;
    private ArrayList<Order> orderList;

    Calendar calendarStart;
    Calendar calendarEnd;
    DatePickerDialog.OnDateSetListener dateStartListener;
    DatePickerDialog.OnDateSetListener dateEndListener;
    RecyclerViewStatiticAdapter recyclerViewStaticAdapter;


    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Static() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Static.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Static newInstance(String param1, String param2) {
        Fragment_Static fragment = new Fragment_Static();
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
        bd = FragmentStaticBinding.inflate(getLayoutInflater());
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();

        setEven();

    }

    private void setEven() {
    }

    private String FormatDate(String date) {
        String formattedDate = null;
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = originalFormat.parse(date);

            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    private String FormatChooseDate(String date) {
        String formattedDate = null;
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date2 = originalFormat.parse(date);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    private void setData() {
       /* //spinner
        String[] items = {"Hoàn thành", "Đã hủy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        bd.spinner.setAdapter(adapter);*/
//        bd.spinner.setOnItemSelectedListener(spinnerListener);
        // recyclerview lấy dữ liệu từ  spinner
        orderList = new ArrayList<>();
        bd.RecyclerViewStatic.setHasFixedSize(true);
        bd.RecyclerViewStatic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewStaticAdapter = new RecyclerViewStatiticAdapter(orderList, getContext());
        bd.RecyclerViewStatic.setAdapter(recyclerViewStaticAdapter);
        reloadOrderWithCondition();


        // Tạo một truy vấn để lấy các đơn hàng có trạng thái là "hoàn thành" và trong khoảng thời gian đã chỉ định

    }

    private void reloadOrderWithCondition() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference customersRef = database.getReference("Customer");

        customersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot Snapshot) {
                for (DataSnapshot customerSnapshot : Snapshot.getChildren()) {
                    String customerId = customerSnapshot.getKey();
                    // Lấy ra tất cả các đơn hàng của khách hàng hiện tại
                    DatabaseReference ordersRef = database.getReference("Customer").child(customerId).child("Order");

                    // Tạo một truy vấn để lấy các đơn hàng trong khoảng thời gian và có trạng thái là "hoàn thành"
                    Query query = ordersRef.orderByChild("orderDate").startAt("2024-04-01").endAt("2024-04-30");
                    /* .orderByChild("status").equalTo("Hoàn thành");*/
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                String status = dataSnapshot.child("status").getValue(String.class);
                                Order o = new Order();
                                String orderDate = dataSnapshot.child("orderDate").getValue(String.class);
                                String orderId = dataSnapshot.child("orderId").getValue(String.class);
                                String totalAmount = dataSnapshot.child("totalAmount").getValue(String.class);
                                o.setOrderId(orderId);
                                o.setOrderDate(orderDate);
                                o.setStatus(status);
                                o.setTotalAmount(totalAmount);
                                if ("Hoàn thành".equals(status)) {
                                    orderList.add(o);
                                }
                            }
                            recyclerViewStaticAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

   /* AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();
            if (selectedItem.equals("Tất cả")) {
                reloadOrder();
            } else if (selectedItem.equals("Chờ duyệt")) {
                reloadOrderWithCondition("Chờ duyệt");
            } else if (selectedItem.equals("Chuẩn bị đơn hàng")) {
                reloadOrderWithCondition("Chuẩn bị đơn hàng");
            } else if (selectedItem.equals("Đang giao")) {
                reloadOrderWithCondition("Đang giao");
            } else if (selectedItem.equals("Hoàn thành")) {
                reloadOrderWithCondition("Hoàn thành");
            } else if (selectedItem.equals("Đã hủy")) {
                reloadOrderWithCondition("Đã hủy");
            }
            //recycleViewManageOrderAdapter.notifyDataSetChanged();
        }

        private void reloadOrderWithCondition(String chờDuyệt) {
        }

        private void reloadOrder() {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }*/
}