package com.example.coffeeshopapp.fragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.FragmentReportShipperBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentReportShipper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentReportShipper extends Fragment {


    private FragmentReportShipperBinding bd;
    private Map<String,String> item =new LinkedHashMap<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String shipperId;
    private String name = "";
    private String phone = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentReportShipper() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentReportShipper.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentReportShipper newInstance(String param1, String param2) {
        FragmentReportShipper fragment = new FragmentReportShipper();
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
        bd = FragmentReportShipperBinding.inflate(getLayoutInflater());
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setEvent() {
        int a = item. size();
        bd.spinnerListShipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Map.Entry<String, String> entry = getElementAtPosition(item, position);
                shipperId =  entry.getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bd.btnNgaybatdau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bd.edtNgaybatdau);
            }
        });
        bd.btnNgaykethuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(bd.edtNgayketthuc);
            }
        });
        bd.btnXuatBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReportShipper();
                } catch (ParseException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        bd.btnXuatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> phoneShipper = new ArrayList<>();
                databaseReference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot accountSnapshot: snapshot.getChildren()){
                            if(accountSnapshot.child("role").getValue(String.class).equals("shipper")){
                                phoneShipper.add(accountSnapshot.getKey());
                            }
                        }
                        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                                    String phone = customerSnapshot.child("account").child("username").getValue(String.class);
                                    if(phoneShipper.stream().anyMatch(i -> i.equals(phone))){
                                        shipperId = customerSnapshot.getKey();
                                        try {
                                            ReportShipper();
                                        } catch (ParseException e) {
                                            throw new RuntimeException(e);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }


    private void setControl() {
        List<String> phoneShipper = new ArrayList<>();
        databaseReference.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot accountSnapshot: snapshot.getChildren()){
                    if(accountSnapshot.child("role").getValue(String.class).equals("shipper")){
                        phoneShipper.add(accountSnapshot.getKey());
                    }
                }
                databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                            String phone = customerSnapshot.child("account").child("username").getValue(String.class);
                            String name = customerSnapshot.child("name").getValue(String.class);
                            if(phoneShipper.stream().anyMatch(i -> i.equals(phone))){
                                item.put(customerSnapshot.getKey(),name + " " + customerSnapshot.getKey());
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, item.values().toArray(new String[0]));
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bd.spinnerListShipper.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        databaseReference.child("Account").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                for(DataSnapshot accountSnapshot: dataSnapshot.getChildren()){
//                    if(accountSnapshot.child("role").getValue(String.class).equals("shipper")){
//                        phoneShipper.add(accountSnapshot.getKey());
//                    }
//                }
//                databaseReference.child("Customer").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        for(DataSnapshot customerSnapshot: dataSnapshot.getChildren()){
//                            String phone = customerSnapshot.child("account").child("username").getValue(String.class);
//                            String name = customerSnapshot.child("name").getValue(String.class);
//                            if(phoneShipper.stream().anyMatch(i -> i.equals(phone))){
//                                item.put(customerSnapshot.getKey(),name + " " + customerSnapshot.getKey());
//                            }
//                        }
//                    }
//                });
//
//            }
//        });
    }
    private void showDatePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set the selected date to the EditText
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editText.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    public static <K, V> Map.Entry<K, V> getElementAtPosition(Map<K, V> map, int position) {
        int index = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (index == position) {
                return entry;
            }
            index++;
        }
        return null;
    }
    private List<Order> soLuongDonHangInTime(String ngayBD, String ngayKT,CountDownLatch  latch) throws ParseException {
        DateFormat Format = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateOfBd = Format.parse(ngayBD);
        Date dateOfKt = Format.parse(ngayKT);
        List<Order> orderList = new ArrayList<>();
        databaseReference.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        Date dateOfOrder = null;
                        try {
                            dateOfOrder = originalFormat.parse(orderDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if(dateOfOrder.after(dateOfBd) && dateOfOrder.before(dateOfKt)){
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String totalAmount = orderSnapshot.child("totalAmount").getValue(String.class);
                            String status =  orderSnapshot.child("status").getValue(String.class);
                            String shipperIdofOrder = orderSnapshot.child("shipperId").getValue(String.class);
                            Order o = new Order();
                            o.setOrderId(orderId);
                            o.setTotalAmount(totalAmount);
                            if(status.equals("Hoàn thành") && shipperIdofOrder.equals(shipperId)){
                                orderList.add(o);
                            }
                        }
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return orderList;
    }

    private String nameOfShipper(CountDownLatch latch){
        databaseReference.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    if(customerSnapshot.getKey().equals(shipperId)){
                        name = customerSnapshot.child("name").getValue(String.class);
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return name;
    }
    private String phoneOfShipper(CountDownLatch latch){
        databaseReference.child("Customer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    if(customerSnapshot.getKey().equals(shipperId)){
                        phone =  customerSnapshot.child("phone").getValue(String.class);
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return phone;
    }
    private void ReportShipper() throws ParseException, InterruptedException {
        String ngayBD = bd.edtNgaybatdau.getText().toString();
        String ngayKT = bd.edtNgayketthuc.getText().toString();
        if(ngayBD.isEmpty() || ngayKT.isEmpty() || shipperId == null || shipperId.isEmpty()){
            Toast.makeText(getContext(),"Vui lòng điền đẩy đủ thông tin", Toast.LENGTH_LONG).show();
            return;
        }
//        List<Order> orderList = soLuongDonHangInTime(ngayBD,ngayKT, latch);

        DateFormat Format = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateOfBd = Format.parse(ngayBD);
        Date dateOfKt = Format.parse(ngayKT);
        List<Order> orderList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                    for(DataSnapshot orderSnapshot: customerSnapshot.child("Order").getChildren()){
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        Date dateOfOrder = null;
                        try {
                            dateOfOrder = originalFormat.parse(orderDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if(dateOfOrder.after(dateOfBd) && dateOfOrder.before(dateOfKt)){
                            String orderId = orderSnapshot.child("orderId").getValue(String.class);
                            String totalAmount = orderSnapshot.child("totalAmount").getValue(String.class);
                            String status =  orderSnapshot.child("status").getValue(String.class);
                            String shipperIdofOrder = orderSnapshot.child("shipperId").getValue(String.class);
                            Order o = new Order();
                            o.setOrderId(orderId);
                            o.setTotalAmount(totalAmount);
                            if(status.equals("Hoàn thành") && shipperIdofOrder.equals(shipperId)){
                                orderList.add(o);
                            }
                        }
                    }
                }
                databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                            if(customerSnapshot.getKey().equals(shipperId)){
                                name = customerSnapshot.child("name").getValue(String.class);
                            }
                        }
                        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot customerSnapshot: snapshot.getChildren()){
                                    if(customerSnapshot.getKey().equals(shipperId)){
                                        phone =  customerSnapshot.child("phone").getValue(String.class);
                                    }
                                }
                                String fileName = String.format("shipper_report%s.pdf",shipperId);
                                Double total = 0.0;
                                for(Order o : orderList){
                                    total+=Double.parseDouble(o.getTotalAmount().replaceAll("\\D",""));
                                }
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                                contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                                ContentResolver resolver = requireContext().getContentResolver();
                                Uri uri = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                                    uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                                }
                                if (uri != null) {
                                    try {
                                        OutputStream outputStream = resolver.openOutputStream(uri);
                                        if (outputStream != null) {
                                            // Tạo một đối tượng Document
                                            Document document = new Document();
                                            // Tạo một PdfWriter để ghi dữ liệu vào tệp PDF
                                            PdfWriter.getInstance(document, outputStream);

                                            // Mở Document để bắt đầu viết vào nó
                                            document.open();

                                            // Thêm tiêu đề và thông tin khoảng thời gian vào Document
                                            document.add(new Paragraph("Shipper Report"));
                                            document.add(new Paragraph("Time: " + ngayBD + " - " + ngayKT));
                                            document.add(new Paragraph("Shipper ID: " + shipperId));
                                            document.add(new Paragraph("Name: " + name));
                                            document.add(new Paragraph("Phone Number: " + phone));
                                            document.add(new Paragraph("Number of Successfully Delivered Orders: " + orderList.size()));
                                            document.add(new Paragraph("Total Shipping Fee: " + (orderList.size()*10000)));
                                            document.add(new Paragraph("Total Order Amount: " + total));

                                            // Đóng Document
                                            document.close();
                                            Toast.makeText(getContext(), "PDF file successfully created", Toast.LENGTH_LONG).show();
                                            // Đóng OutputStream
                                            outputStream.close();
                                        }
                                    } catch (IOException | DocumentException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Error: Unable to create PDF file", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(getContext(), "PDF file successfully created", Toast.LENGTH_LONG).show();
    }
}