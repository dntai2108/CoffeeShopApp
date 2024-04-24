package com.example.coffeeshopapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.HomeActivity;
import com.example.coffeeshopapp.activity.LoginActivity;
import com.example.coffeeshopapp.activity.ProfileActivity;
import com.example.coffeeshopapp.databinding.FragmentAccountActivityShipperBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccountActivityShipper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccountActivityShipper extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAccountActivityShipperBinding binding;

    public FragmentAccountActivityShipper() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccountActivityShipper.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccountActivityShipper newInstance(String param1, String param2) {
        FragmentAccountActivityShipper fragment = new FragmentAccountActivityShipper();
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
        binding = FragmentAccountActivityShipperBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEven();
    }

    private void setEven() {
        binding.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        binding.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HomeActivity.class));

            }
        });
    }
}