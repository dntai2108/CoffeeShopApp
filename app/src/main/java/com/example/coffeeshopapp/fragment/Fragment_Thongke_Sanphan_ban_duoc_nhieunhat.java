package com.example.coffeeshopapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.R;

import com.example.coffeeshopapp.adapter.RecyclerViewThongKeSanPhamBanNhieuNhatAdapter;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Thongke_Sanphan_ban_duoc_nhieunhat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Thongke_Sanphan_ban_duoc_nhieunhat extends Fragment {
    // khai báo
    ArrayList<Product> productArrayList;
    RecyclerView recyclerView_sanpham_banduoc_nhieunhat;
    RecyclerViewThongKeSanPhamBanNhieuNhatAdapter adapter_SanPham_banduoc_nhieunhat;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Thongke_Sanphan_ban_duoc_nhieunhat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Thongke_Sanphan_ban_duoc_nhieunhat.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Thongke_Sanphan_ban_duoc_nhieunhat newInstance(String param1, String param2) {
        Fragment_Thongke_Sanphan_ban_duoc_nhieunhat fragment = new Fragment_Thongke_Sanphan_ban_duoc_nhieunhat();
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
        return inflater.inflate(R.layout.fragment__thongke__sanphan_ban_duoc_nhieunhat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productArrayList = new ArrayList<>();
        recyclerView_sanpham_banduoc_nhieunhat=view.findViewById(R.id.recyclerviewThongke_sanpham_bestseler);
        recyclerView_sanpham_banduoc_nhieunhat.setHasFixedSize(true);
        recyclerView_sanpham_banduoc_nhieunhat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Product");
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    String image = productSnapshot.child("image").getValue(String.class);
                    String name = productSnapshot.child("name").getValue(String.class);
                    String price = productSnapshot.child("price").getValue(String.class);
                    String soluongmua = productSnapshot.child("soluongmua").getValue(String.class);
                    Product p = new Product();
                    p.setImage(image);
                    p.setName(name);
                    p.setPrice(price);
                    p.setSoluongmua(soluongmua);
                    productArrayList.add(p);
                }
                // Sau khi lấy dữ liệu, sắp xếp productList theo số lượng đã bán giảm dần
                Collections.sort(productArrayList, (p1, p2) -> Integer.compare(Integer.parseInt(p2.getSoluongmua()), Integer.parseInt(p1.getSoluongmua())));

                // Gắn adapter cho RecyclerView
                // adapter = new ProductAdapter(productList, getContext());
                //recyclerView.setAdapter(adapter);
                adapter_SanPham_banduoc_nhieunhat = new RecyclerViewThongKeSanPhamBanNhieuNhatAdapter(productArrayList, getContext());
                recyclerView_sanpham_banduoc_nhieunhat.setAdapter(adapter_SanPham_banduoc_nhieunhat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}