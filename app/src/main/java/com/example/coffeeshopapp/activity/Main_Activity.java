package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.coffeeshopapp.DAO.OrderDAO;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main_Activity extends AppCompatActivity {
    ListView listView, lv_homework;
    HashMap<Integer,String> items;
    HashMap<Integer,String> item_homework;
    int selected_id_item = 0;
    int selected_id_item_hw = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lvgiuaky);
        lv_homework = findViewById(R.id.lv_baitap);

//        Order order1 = new Order(15,"2024-04-02 11:19",125000,2);
//        Order order2 = new Order(16,"2024-04-02 13:20",25000,2);
//        Order order3 = new Order(17,"2024-04-03 13:35",75000,3);
//        Order order4 = new Order(18,"2024-04-03 14:15",55000,3);
//        Order order5 = new Order(19,"2024-04-04 14:06",25000,2);
//        Order order6 = new Order(20,"2024-04-04 14:45",50000,3);
//        Order order7 = new Order(21,"2024-04-05 15:50",75000,3);
//        Order order8 = new Order(22,"2024-04-05 19:27",95000,3);
//
//        OrderDAO orderDAO = new OrderDAO(this);
//        orderDAO.addOrder(order1);
//        orderDAO.addOrder(order2);
//        orderDAO.addOrder(order3);
//        orderDAO.addOrder(order4);
//        orderDAO.addOrder(order5);
//        orderDAO.addOrder(order6);
//        orderDAO.addOrder(order7);
//        orderDAO.addOrder(order8);
        Set_Data();
        Set_Data_Homework();
        Transform_Activity();








    }



    private void Set_Data_Homework() {
        item_homework = new HashMap<>();
        item_homework.put(1,"Bài 4");
        item_homework.put(2,"Bài 8");
        ArrayList<String> display_item_hw = new ArrayList<>(item_homework.values());
        ArrayAdapter<String> item_adapter = new ArrayAdapter<>((Context) this, android.R.layout.simple_list_item_1, display_item_hw);
        lv_homework.setAdapter(item_adapter);
    }

    private void Set_Data() {
        items = new HashMap<>();
        items.put(1,"Thống kê đơn hàng");
        items.put(2,"Thống kê sản phẩm");
        items.put(3,"Thống kê doanh thu");
        ArrayList<String> display_item = new ArrayList<>(items.values());
        ArrayAdapter<String> item_adapter = new ArrayAdapter<>((Context) this, android.R.layout.simple_list_item_1, display_item);
        listView.setAdapter(item_adapter);
    }

    private void Set_Intent(Class<?> new_activity_class) {
        Intent intent = new Intent(Main_Activity.this, new_activity_class);
        startActivity(intent);
    }

    private void Transform_Activity() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position);
                selected_id_item = getKeyByValue(items, selected_item);
                if(selected_id_item == 1) {
                    Set_Intent(StatisticsActivity.class);
                } else if(selected_id_item == 2) {
                    Set_Intent(ProductStatiticsAtivity.class);
                } else {
                    Set_Intent(Revenue_Statistics_Activity.class);
                }
            }
        });
    }


    public static int getKeyByValue(HashMap<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1; // hoặc giá trị mặc định phù hợp với ứng dụng của bạn
    }
}