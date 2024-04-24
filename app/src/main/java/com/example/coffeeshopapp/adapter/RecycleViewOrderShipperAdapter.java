package com.example.coffeeshopapp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Order;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecycleViewOrderShipperAdapter extends RecyclerView.Adapter<RecycleViewOrderShipperAdapter.ViewHolder> {

    private final ArrayList<Order> orderList;
    private final Context context;

    public RecycleViewOrderShipperAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView idorder,ordertime,status,total;
        Button btnDuyet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idorder = itemView.findViewById(R.id.idorder);
            ordertime = itemView.findViewById(R.id.ordertime);
            status = itemView.findViewById(R.id.status);
            total = itemView.findViewById(R.id.total);
            btnDuyet = itemView.findViewById(R.id.btnDuyet);
        }
    }


    @NonNull
    @Override
    public RecycleViewOrderShipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_ordershipper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewOrderShipperAdapter.ViewHolder holder, int position) {
        Order o = orderList.get(position);
        holder.idorder.setText(o.getOrderId());
        holder.ordertime.setText(o.getOrderDate());
        holder.status.setText(o.getStatus());
        holder.total.setText(o.getTotalAmount());
        holder.btnDuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Customer");
                databaseReferences.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
                        String userId = sharedPreferences.getString("userId", "");
                        for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                            for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                                String orderId = orderSnapshot.child("orderId").getValue(String.class);
                                if(orderId.equals(o.getOrderId())){
                                    orderSnapshot.getRef().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.child("status").getRef().setValue("Chuẩn bị đơn hàng");
                                            snapshot.child("shipperId").getRef().setValue(userId);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(context,"Duyệt hàng thành công", Toast.LENGTH_LONG).show();
                                    databaseReferences.removeEventListener(this);
                                    return;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
