package com.example.coffeeshopapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.adapter.ItemAdapter;
import com.example.coffeeshopapp.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_trangchu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_trangchu extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvSanPham;
    ItemAdapter itemAdapter;

    private ArrayList<Product> productList;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public Fragment_trangchu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_trangchu.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_trangchu newInstance(String param1, String param2) {
        Fragment_trangchu fragment = new Fragment_trangchu();
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
        return inflater.inflate(R.layout.fragment_trangchu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSanPham = view.findViewById(R.id.rvSanPham);
        productList = new ArrayList<>();
        itemAdapter = new ItemAdapter(productList, getContext());
        reloadProduct();
        setEven();
    }

    private void setEven() {
        rvSanPham.setHasFixedSize(true);
        rvSanPham.setLayoutManager(new GridLayoutManager(getContext(), 2));
        itemAdapter = new ItemAdapter(productList, getContext());
        rvSanPham.setAdapter(itemAdapter);
    }

    private void reloadProduct() {

        databaseReference.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                    productList.sort(new Comparator<Product>() {
                        @Override
                        public int compare(Product o1, Product o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                }
                itemAdapter.setData(productList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}