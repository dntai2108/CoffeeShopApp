package com.example.coffeeshopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.ManageUser;
import com.example.coffeeshopapp.model.Account;
import com.example.coffeeshopapp.model.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecycleViewManageUserAdapter extends RecyclerView.Adapter<RecycleViewManageUserAdapter.ViewHolder> {
    private final ArrayList<Customer> customerList;
    private final Context context;

    public RecycleViewManageUserAdapter(ArrayList<Customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgUser,imgActivity;
        TextView tvName,tvSDT,tvActivity,tvLock,tvOrder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            imgActivity = itemView.findViewById(R.id.imgActivity);
            tvName = itemView.findViewById(R.id.tvName);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvActivity = itemView.findViewById(R.id.tvActivity);
            tvLock = itemView.findViewById(R.id.tvLock);
            tvOrder = itemView.findViewById(R.id.tvOrder);
        }
    }
    @NonNull
    @Override
    public RecycleViewManageUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_khachhang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewManageUserAdapter.ViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvName.setText(customer.getName());
        Account a = customer.getAccount();
        boolean b = customer.getAccount().isState();
        if(customer.getAccount().isState()){
            holder.tvActivity.setText("Hoạt động");
            holder.imgActivity.setImageResource(R.drawable.hoatdong);
            holder.tvLock.setText("Khóa");
        }
        else{
            holder.tvActivity.setText("Không hoạt động");
            holder.imgActivity.setImageResource(R.drawable.khonghoatdong);
            holder.tvLock.setText("Mở khóa");
        }
        holder.tvSDT.setText(customer.getPhone());
        holder.tvLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseAcountReferences  = FirebaseDatabase.getInstance().getReference("Customer");
                if(customer.getAccount().isState()){
                    databaseAcountReferences.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                Customer customerSnapshot = dataSnapshot.getValue(Customer.class);
                                if(customerSnapshot.getId().equals(customer.getId())){
                                    Account a = customerSnapshot.getAccount();
                                    a.setState(false);
                                    DatabaseReference databaseReference = dataSnapshot.getRef();
                                    databaseReference.child("account").setValue(a);
                                    databaseReference  = FirebaseDatabase.getInstance().getReference("Account");
                                    databaseReference.child(customerSnapshot.getAccount().getUsername()).setValue(a);
                                    Toast.makeText(context,"Đổi trạng thái thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    databaseAcountReferences.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                Customer customerSnapshot = dataSnapshot.getValue(Customer.class);
                                if(customerSnapshot.getId().equals(customer.getId())){
                                    Account a = customerSnapshot.getAccount();
                                    a.setState(true);
                                    DatabaseReference databaseReference = dataSnapshot.getRef();
                                    databaseReference.child("account").setValue(a);
                                    databaseReference  = FirebaseDatabase.getInstance().getReference("Account");
                                    databaseReference.child(customerSnapshot.getAccount().getUsername()).setValue(a);
                                    Toast.makeText(context,"Đổi trạng thái thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
