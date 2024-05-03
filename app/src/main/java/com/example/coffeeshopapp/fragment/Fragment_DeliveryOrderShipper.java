package com.example.coffeeshopapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.GiaoHang;
import com.example.coffeeshopapp.databinding.FragmentDeliveryOrderShipperBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_DeliveryOrderShipper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_DeliveryOrderShipper extends Fragment {
    private FragmentDeliveryOrderShipperBinding bd;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_DeliveryOrderShipper() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_DeliveryOrderShipper.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_DeliveryOrderShipper newInstance(String param1, String param2) {
        Fragment_DeliveryOrderShipper fragment = new Fragment_DeliveryOrderShipper();
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
        bd = FragmentDeliveryOrderShipperBinding.inflate(getLayoutInflater());
        if (savedInstanceState == null) {
            replaceFragment(new Fragment_Delivering());
        }
        bd.navshipper.setOnItemSelectedListener(item -> {
            int itemId=item.getItemId();
            if(itemId==R.id.nav_danggiao){
                replaceFragment(new Fragment_Delivering());
            }
            if(itemId==R.id.nav_dagiao){
                replaceFragment(new Fragment_Deliveried());
            }
            return true;
        });
        bd.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), GiaoHang.class);
                getContext().startActivity(intent);
            }
        });
        return bd.getRoot();
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager= getChildFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayoutOrder,fragment);
        fragmentTransaction.commit();
    }
}