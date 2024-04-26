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
import com.example.coffeeshopapp.activity.AccountActivityAdmin;
import com.example.coffeeshopapp.activity.BottomNavAdmin;
import com.example.coffeeshopapp.activity.ChangePassword;
import com.example.coffeeshopapp.activity.ChangePasswordAdmin;
import com.example.coffeeshopapp.activity.LoginActivity;
import com.example.coffeeshopapp.activity.ManageUser;
import com.example.coffeeshopapp.activity.ProfileActivity;
import com.example.coffeeshopapp.activity.ProfileAdminActivity;
import com.example.coffeeshopapp.databinding.FragmentAccountAdminBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccountAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccountAdmin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentAccountAdminBinding bd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAccountAdmin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccountAdmin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccountAdmin newInstance(String param1, String param2) {
        FragmentAccountAdmin fragment = new FragmentAccountAdmin();
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
        bd = FragmentAccountAdminBinding.inflate(getLayoutInflater());
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bd.tvManageCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManageUser.class);
                startActivity(intent);
            }
        });

        bd.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileAdminActivity.class);
                startActivity(intent);
            }
        });

        bd.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

        bd.tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePasswordAdmin.class));
            }
        });

        bd.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BottomNavAdmin.class);
                intent.putExtra("openTaiKhoan", true);
                startActivity(intent);
            }
        });
    }
}