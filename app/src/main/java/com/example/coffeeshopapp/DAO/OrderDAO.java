package com.example.coffeeshopapp.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.coffeeshopapp.Database;
import com.example.coffeeshopapp.model.Order;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

// LẤY DỮ LIỆU TOTAL ORDER BY YEAR
// ĐỂ TRUYỀN DỮ LIỆU LÊN
public class OrderDAO {

    //    private Database dbHelper;
//    private SQLiteDatabase database;
    Context context;
    int size_order = 0;
    ArrayList<Order> orderList = new ArrayList<>();

    public OrderDAO(Context context) {
//        dbHelper = new Database(context);
        this.context = context;
//        database = dbHelper.getWritableDatabase(); // Mở cơ sở dữ liệu để ghi
    }

    // LẤY TOÀN BỘ DANH SÁCH
    public void Get_All_Order(OrderDataListener listener) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        orderList.clear();
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    Order order = data.getValue(Order.class);
                    orderList.add(order);
                }
                listener.onOrderListLoaded(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to read data from Firebase Database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Thêm dữ liệu vào bảng "Order"
    public void addOrder(Order order) {
        Get_All_Order(new OrderDataListener() {
            @Override
            public void onOrderListLoaded(ArrayList<Order> orderList) {
                int size = orderList.size();
                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child("Order " + size);
                orderRef.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(context, "Successfully Add data", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNumberList(ArrayList<Integer> yearList) {

            }
        });

    }

    public Date FormatDate(String day) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            return inputFormat.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Date Format_String_to_Date(String day) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(day);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void getAllOrders(String ordertimestart, String ordertimeend, String status, OrderDataListener listener) {
        Get_All_Order(new OrderDataListener() {
            @Override
            public void onOrderListLoaded(ArrayList<Order> orderList) {
                ArrayList<Order> order_list_time = new ArrayList<>();

                for (Order order : orderList) {
                    if (order.getStatus().equals(status) && isOrderInTimeRange(order, ordertimestart, ordertimeend)) {
                        order_list_time.add(order);
                    }
                }
                listener.onOrderListLoaded(order_list_time);
            }

            @Override
            public void onNumberList(ArrayList<Integer> yearList) {

            }
        });
    }

    private boolean isOrderInTimeRange(Order order, String ordertimestart, String ordertimeend) {
        long orderTime = FormatDate(order.getOrderDate()).getTime();
        long startTime = Format_String_to_Date(ordertimestart).getTime();
        long endTime = Format_String_to_Date(ordertimeend).getTime();
        return orderTime >= startTime && orderTime <= endTime;
    }

    public ArrayList<Integer> getAllStatus() {
        ArrayList<Integer> StatusList = new ArrayList<>();

//        Get_All_Order(new OrderDataListener() {
//            @Override
//            public void onOrderListLoaded(ArrayList<Order> orderList) {
//
//
//                for(Order order : orderList) {
//                    if(order.getStatus() == status && isOrderInTimeRange(order, ordertimestart, ordertimeend)) {
//                        order_list_time.add(order);
//                    }
//                }
//                listener.onOrderListLoaded(order_list_time);
//            }
//        });
//        Cursor cursor = database.rawQuery("SELECT ID, OrderTime, ToTal FROM Orders WHERE Status = 2", null);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                // Lấy dữ liệu từ cursor và tạo đối tượng Order
//                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
//                @SuppressLint("Range") String OrderTime = cursor.getString(cursor.getColumnIndex("OrderTime"));
//                @SuppressLint("Range") double ToTal = cursor.getDouble(cursor.getColumnIndex("Total"));
//                Order order = new Order(id, OrderTime, ToTal, 3);
//                // Thêm đối tượng Order vào danh sách
//                StatusList.add(1);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
        return StatusList;
    }

    public CompletableFuture<LinkedHashMap<String, Double>> GetTotalOrdersByMonth(int year1) {
        CompletableFuture<LinkedHashMap<String, Double>> future = new CompletableFuture<>();

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        LinkedHashMap<String, Double> totalOrders = new LinkedHashMap<>();

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    assert order != null;
                    String year = String.valueOf(Format_Year(order.getOrderDate()));
                    String month = String.valueOf(Format_Month(order.getOrderDate()));
                    double total = Double.parseDouble(order.getTotalAmount());

                    if (order.getStatus().equalsIgnoreCase("Hoàn thành")) {
                        if (year.equals(String.valueOf(year1))) {
                            totalOrders.merge(month, total, Double::sum);
                        }

                    }
                }
                future.complete(totalOrders); // Hoàn tất CompletableFuture với dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(error.toException()); // Xử lý lỗi nếu có
            }
        });

        return future;
    }

    public CompletableFuture<LinkedHashMap<String, Double>> GetTotalOrdersByYear() {
        CompletableFuture<LinkedHashMap<String, Double>> future = new CompletableFuture<>();

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        LinkedHashMap<String, Double> totalOrders = new LinkedHashMap<>();

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    String year = String.valueOf(Format_Year(order.getOrderDate()));
                    double total = Double.parseDouble(order.getTotalAmount());

                    if (order.getStatus().equalsIgnoreCase("Hoàn thành")) {
                        totalOrders.merge(year, total, Double::sum);
                    }
                }
                future.complete(totalOrders); // Hoàn tất CompletableFuture với dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(error.toException()); // Xử lý lỗi nếu có
            }
        });

        return future;
    }

    public LinkedHashMap<String, Double> GetTotalOrdersByDay(int month, int year) {
        LinkedHashMap<String, Double> total_orders = new LinkedHashMap<>();

//        String[] selectionArgs = {TransToString(month), String.valueOf(year)};
//        Cursor cursor = database.rawQuery("SELECT STRFTIME('%d', OrderTime) AS Day, SUM(Total) AS TotalOrder \n" +
//                        "FROM Orders \n" +
//                        "WHERE STRFTIME('%m', OrderTime) = ? AND STRFTIME('%Y', OrderTime) = ? AND Status = 3 \n" +
//                        "GROUP BY STRFTIME('%d', OrderTime);"
//                , selectionArgs);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                // Lấy dữ liệu từ cursor và tạo đối tượng Order
//
//                @SuppressLint("Range") String OrderTime = cursor.getString(cursor.getColumnIndex("Day"));
//                @SuppressLint("Range") double Total = cursor.getDouble(cursor.getColumnIndex("TotalOrder"));
//
//                // Thêm đối tượng Order vào danh sách
//                total_orders.put(OrderTime,Total);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
        return total_orders;
    }

    public int Format_Year(String dateTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date = format.parse(dateTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Trả về giá trị không hợp lệ nếu có lỗi phân tích cú pháp
        }
    }

    public int Format_Month(String dateTime) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date = format.parse(dateTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH) + 1; // Tháng được trả về từ 0 đến 11, nên cần cộng thêm 1 để chuyển đổi thành tháng từ 1 đến 12.
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Trả về giá trị không hợp lệ nếu có lỗi phân tích cú pháp
        }
    }

    // Hàm lấy Year
    public void Get_Year(OrderDataListener listener) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        ArrayList<Integer> list_year = new ArrayList<>();
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    assert order != null;
                    if (!list_year.contains(Format_Year(order.getOrderDate()))) {
                        list_year.add(Format_Year(order.getOrderDate()));
                    }
                }
                // Gọi hàm gọi lại để trả về danh sách năm
                listener.onNumberList(list_year);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to read data from Firebase Database", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public CompletableFuture<ArrayList<Integer>> Get_Month_ByYear(int year1) {

        CompletableFuture<ArrayList<Integer>> future = new CompletableFuture<>();

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        ArrayList<Integer> List_month = new ArrayList<>();

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Order order = data.getValue(Order.class);
                    assert order != null;
                    String year = String.valueOf(Format_Year(order.getOrderDate()));
                    String month = String.valueOf(Format_Month(order.getOrderDate()));


                    if (year.equals(String.valueOf(year1))) {

                        if (!List_month.contains(Integer.parseInt(month))) {
                            List_month.add(Integer.parseInt(month));
                        }

                    }
                }

                future.complete(List_month); // Hoàn tất CompletableFuture với dữ liệu
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                future.completeExceptionally(error.toException()); // Xử lý lỗi nếu có
            }
        });

        return future;
    }


    public ArrayList<Integer> Get_Day_ByMonthAndYear(int month, int year) {
        ArrayList<Integer> list_day = new ArrayList<>();
//        String[] selectionArgs = {TransToString(month),String.valueOf(year)};
//        Cursor cursor = database.rawQuery("SELECT DISTINCT STRFTIME('%d', OrderTime) AS Day FROM Orders WHERE STRFTIME('%m', OrderTime) = ? AND STRFTIME('%Y', OrderTime) = ? Order by STRFTIME('%d', OrderTime) ASC;", selectionArgs);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                // Lấy dữ liệu từ cursor và tạo đối tượng Order
//
//                @SuppressLint("Range") int day = cursor.getInt(cursor.getColumnIndex("Day"));
//                list_day.add(day);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
        return list_day;
    }

}



