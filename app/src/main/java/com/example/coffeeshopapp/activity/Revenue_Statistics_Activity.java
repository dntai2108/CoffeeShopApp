package com.example.coffeeshopapp.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshopapp.DAO.HashMapList;
import com.example.coffeeshopapp.DAO.OrderDAO;
import com.example.coffeeshopapp.DAO.OrderDataListener;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Order;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Revenue_Statistics_Activity extends AppCompatActivity {
    LineChart linechart;
    XAxis axis;
    private ArrayList<String> xValues;
    OrderDAO orderDAO;
    ImageButton btnBack;
    Spinner spinner_type_time, spinner_year;
    ArrayList<Integer> year;
    Spinner spinner_month;
    HashMap<Integer, String> type_time, months;

    //    int key_default_year = 0;
    int selected_idtypetime = 0;
    int selected_year = 0;
    int selected_month = 0;
    int size_year = 0;
    List<Entry> entries;
    HashMap<String, Double> totalOrder;
    ArrayList<String> years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_statistics);

        Set_Component();//set thành phần giao diện
        Set_Declare();// định nghĩa thành phần
        Setup_Data_Sprinner_Typetime();// Set up data spinner typetime
        selected_idtypetime = Get_Key_Default(spinner_type_time, type_time);// đặt mặc định cho selected_idtypetime
        Get_Data_Spinner_Year();
        Setup_Data_Spinner_Year(); // tương tự
//        selected_year = Get_Key_Year_Default();
//        Set_Up_Data_Spinner_Month();
//        selected_month = Get_Key_Default(spinner_month,months);
        Set_Description_Linechart();
//        Set_up_Default_X();
        Set_up_Y();
        Load_Data_Default();
        Set_Up_Spinner_Typetime();
        Set_Up_Spinner_Year();
//        Set_Up_Spinner_Month();
        Set_BtnBack();

    }

    private void Set_BtnBack() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void Set_Up_Spinner_Month() {
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String display_month = (String) parent.getItemAtPosition(position);
                selected_month = getKeyByValue(months, display_month);
                totalOrder.clear();
                Set_up_X();
                totalOrder = orderDAO.GetTotalOrdersByDay(selected_month, selected_year);
                if (totalOrder.isEmpty()) {
                    Clear_Data();
                } else {
                    Set_Data();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Set_Up_Spinner_Year() {
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String year = String.valueOf(parent.getItemAtPosition(position));
                selected_year = Integer.parseInt(year);

                totalOrder.clear();
                if (selected_idtypetime == 2) {
                    Set_up_X();
                    orderDAO.GetTotalOrdersByMonth(Integer.parseInt(year)).thenAccept(hashMap -> {
                        for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
                            String year1 = entry.getKey();
                            Double total = entry.getValue();
                            totalOrder.put(year1, total);
                        }

                        if (totalOrder.isEmpty()) {
                            Clear_Data();
                        } else {
                            Set_Data();
                        }
                    });

                } else if (selected_idtypetime == 3) {
//                    Set_up_X();
                    totalOrder = orderDAO.GetTotalOrdersByDay(selected_month, selected_year);
                    if (totalOrder.isEmpty()) {
                        Clear_Data();
                    } else {
                        Set_Data();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(Revenue_Statistics_Activity.this, "You haven't choose item yet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Set_Up_Data_Spinner_Month() {
        months.put(1, "Tháng 1");
        months.put(2, "Tháng 2");
        months.put(3, "Tháng 3");
        months.put(4, "Tháng 4");
        months.put(5, "Tháng 5");
        months.put(6, "Tháng 6");
        months.put(7, "Tháng 7");
        months.put(8, "Tháng 8");
        months.put(9, "Tháng 9");
        months.put(10, "Tháng 10");
        months.put(11, "Tháng 11");
        months.put(12, "Tháng 12");

        String firstValue = months.get(months.keySet().toArray()[0]);
        ArrayList<String> display_month = new ArrayList<>(months.values());
        int posotion = new ArrayList<>(months.values()).indexOf(firstValue);
        ArrayAdapter<String> months_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, display_month);
        months_Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_month.setAdapter(months_Adapter);
        spinner_month.setSelection(posotion);

    }

    private void Set_Up_Spinner_Typetime() {
        spinner_type_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_time = (String) parent.getItemAtPosition(position);

                selected_idtypetime = getKeyByValue(type_time, selected_time);


                if (selected_idtypetime == 1) {
                    Set_up_X();
                    spinner_year.setVisibility(View.GONE);
                    spinner_month.setVisibility(View.GONE);
                    Load_Data_Default();


                } else if (selected_idtypetime == 2) {
                    Set_up_X();
                    spinner_year.setVisibility(View.VISIBLE);
                    totalOrder.clear();
                    orderDAO.GetTotalOrdersByMonth(2024).thenAccept(hashMap -> {
                        for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
                            String year = entry.getKey();
                            Double total = entry.getValue();
                            totalOrder.put(year, total);
                        }


                        Load_Data_Y_ByYear();
                        Display_Chart();
                    });
                    if (totalOrder.isEmpty()) {
                        Clear_Data();
                    } else {
                        Set_Data();
                    }
                } else {

                    spinner_month.setVisibility(View.VISIBLE);
                    spinner_year.setVisibility(View.VISIBLE);
//                    Set_up_X();
                    totalOrder.clear();
                    totalOrder = orderDAO.GetTotalOrdersByDay(selected_month, selected_year);
                    if (totalOrder.isEmpty()) {
                        Clear_Data();
                    } else {
                        Set_Data();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Load_Data_Default() {
        Set_up_X();
        spinner_year.setVisibility(View.GONE);
        entries.clear();
        totalOrder.clear();

        orderDAO.GetTotalOrdersByYear().thenAccept(hashMap -> {
            for (Map.Entry<String, Double> entry : hashMap.entrySet()) {
                String year = entry.getKey();
                Double total = entry.getValue();
                totalOrder.put(year, total);
            }


            Load_Data_Y_ByYear();
            Display_Chart();
        });
    }


    private void Set_up_Y() {
        YAxis yAxis = linechart.getAxisLeft();
        yAxis.setAxisMinimum(100f);
        yAxis.setAxisMaximum(1000000f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.RED);
        yAxis.setLabelCount(10);
    }

    private void Set_Description_Linechart() {
        Description description = new Description();
        description.setText("VND");
        description.setPosition(150f, 15f);
        linechart.setDescription(description);
        linechart.getAxisRight().setDrawLabels(false);
    }

    private void Set_Declare() {
        axis = linechart.getXAxis();
        type_time = new HashMap<>();
        years = new ArrayList<>();
        months = new HashMap<>();
        xValues = new ArrayList<>();
        totalOrder = new HashMap<>();
        entries = new ArrayList<>();
        orderDAO = new OrderDAO(this);
        year = new ArrayList<>();

    }

    @SuppressLint("WrongViewCast")
    private void Set_Component() {
        linechart = findViewById(R.id.chart);
        spinner_type_time = findViewById(R.id.idselecttypetime);
        spinner_year = findViewById(R.id.idyear);
        spinner_month = findViewById(R.id.id_select_month);
        btnBack = findViewById(R.id.btnBack);
    }

    private void Setup_Data_Sprinner_Typetime() {

        type_time.put(1, "Năm");
        type_time.put(2, "Tháng");
        type_time.put(3, "Ngày");
        String firstValue = type_time.get(type_time.keySet().toArray()[0]);
        ArrayList<String> display_typetime = new ArrayList<>(type_time.values());
        int posotion = new ArrayList<>(type_time.values()).indexOf(firstValue);
        ArrayAdapter<String> typetime_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, display_typetime);
        typetime_Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_type_time.setAdapter(typetime_Adapter);
        spinner_type_time.setSelection(posotion);

    }

    private void Get_Data_Spinner_Year() {


    }

    private void Setup_Data_Spinner_Year() {
        ArrayList<Integer>yearList = new ArrayList<Integer>();
        yearList.add(2024);
        ArrayAdapter<Integer> year_Adapter = new ArrayAdapter<>(Revenue_Statistics_Activity.this, android.R.layout.simple_spinner_item, yearList);
        year_Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_year.setAdapter(year_Adapter);
    }

    private void Load_Data_Y_ByYear() {
        int i = 1;
        for (Map.Entry<String, Double> entry : totalOrder.entrySet()) {
            double revenue = entry.getValue();
            entries.add(new Entry(i, (float) revenue));
            i++;
        }

    }

    private void Display_Chart() {
        LineDataSet dataset1 = new LineDataSet(entries, "Doanh Thu(VND)");
        dataset1.setColor(Color.BLUE);
        LineData lineData = new LineData(dataset1);
        linechart.setData(lineData);
        linechart.invalidate();
    }

    private void Set_up_X() {

        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setGranularity(1f);
        if (selected_idtypetime == 1) {
            xValues.clear();
//            xValues.add("");
            orderDAO.Get_Year(new OrderDataListener() {
                @Override
                public void onOrderListLoaded(ArrayList<Order> orderList) {

                }

                @Override
                public void onNumberList(ArrayList<Integer> yearList) {
                    HashSet<Integer> hashSet = new HashSet<>(yearList);
                    ArrayList<Integer> uniqueList = new ArrayList<>(hashSet);
                    Collections.sort(uniqueList);
                    for (Integer year : yearList) {
                        xValues.add(String.valueOf(year));
                    }
                    axis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                    axis.setLabelCount(yearList.size());
                    axis.setAxisLineColor(Color.RED);
                }
            });


        } else if (selected_idtypetime == 2) {
            xValues.clear();
            xValues.add(""); // Thêm giá trị rỗng vào đầu danh sách

            orderDAO.Get_Month_ByYear(selected_year).thenAccept(arrayList -> {
                // Sắp xếp danh sách các tháng tăng dần
                Collections.sort(arrayList);

                // Thêm các tháng đã sắp xếp vào danh sách xValues
                for (Integer month : arrayList) {

                    xValues.add(String.valueOf(month));
                }

                // Cập nhật định dạng của trục x
                axis.setValueFormatter(new IndexAxisValueFormatter(xValues));
                axis.setLabelCount(arrayList.size());
            });
        } else if (selected_idtypetime == 3) {
            xValues.clear();
            xValues.add("");
            for (Integer month : orderDAO.Get_Day_ByMonthAndYear(selected_month, selected_year)) {
                xValues.add(String.valueOf(month));
            }

            axis.setValueFormatter(new IndexAxisValueFormatter(xValues));
            axis.setLabelCount(orderDAO.Get_Day_ByMonthAndYear(selected_month, selected_year).size());
        }
    }

    private void Set_up_Default_X(ArrayList<Integer> list_year) {

        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setGranularity(1f);

        xValues.add("");
        for (Integer year : list_year) {
            xValues.add(String.valueOf(year));
        }
        axis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        axis.setLabelCount(list_year.size());
    }

    private void Clear_Data() {
        entries.clear();
        LineDataSet dataset1 = new LineDataSet(entries, null);
        dataset1.setColor(Color.BLUE);
        LineData lineData = new LineData(dataset1);
        linechart.setData(lineData);
        linechart.invalidate();
    }

    private void Set_Data() {
        entries.clear();
        Load_Data_Y_ByYear();
        Display_Chart();
    }

    private int Get_Key_Default(Spinner spinner, HashMap<Integer, String> list_data) {
        int defaultPosition = spinner.getSelectedItemPosition();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        return getKeyByValue(list_data, adapter.getItem(defaultPosition));
    }

    private int Get_Key_Year_Default() {
        int default_year_position = spinner_year.getSelectedItemPosition();
        ArrayAdapter<Integer> year_adapter = (ArrayAdapter<Integer>) spinner_year.getAdapter();
        int default_year = year_adapter.getItem(default_year_position);
        assert default_year != 0;
        return default_year; // Chuyển đổi chuỗi thành số nguyên
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