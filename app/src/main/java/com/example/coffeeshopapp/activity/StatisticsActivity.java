package com.example.coffeeshopapp.activity;

import static com.example.coffeeshopapp.activity.Revenue_Statistics_Activity.getKeyByValue;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopapp.Adapter.OrderAdapter;
import com.example.coffeeshopapp.DAO.OrderDAO;
import com.example.coffeeshopapp.DAO.OrderDataListener;
import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
//    String DB_PATH_SUFFIX = "/databases/";
//    SQLiteDatabase database = null;
//    String DATABASE_NAME = "DatabaseAppCoffee.db";

    TextView txtdaystart;
    TextView txtdayend;
    ImageButton btndaystart;
    ImageButton btnsdayend;
    ImageButton btnBack;
    Spinner spinner;

    ListView lv;
    Calendar calendarStart;
    Calendar calendarEnd;
    SimpleDateFormat dateFormat;
    DatePickerDialog.OnDateSetListener dateStartListener;
    DatePickerDialog.OnDateSetListener dateEndListener;
    OrderDAO orderDAO;
    Context context;
    HashMap<Integer, String> status = new HashMap<>();
    ;
    int selectedId = -1;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        AnhXa();
        orderDAO = new OrderDAO(this);
//        processCopy();
        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        context = this;

        SetupSprinner();
        ChooseDayStart();
        ChooseDayEnd();
        getStatusSpinner();
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

    public static String selectedDisplay = "";

    public void getStatusSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = (String) parent.getItemAtPosition(position);
                selectedId = getKeyByValue(status, selectedStatus);
                selectedDisplay = selectedStatus;
                updateOrderList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });
    }

    private String FormatDate(String day) {

        String formattedDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {

            Date date = inputFormat.parse(day);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    private void SetupSprinner() {


        status.put(3, "Hoàn thành");
        status.put(2, "Đã huỷ");

        ArrayList<String> displayStatus = new ArrayList<>(status.values());
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, displayStatus);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(statusAdapter);
    }

    @SuppressLint("WrongViewCast")
    private void AnhXa() {
        btndaystart = findViewById(R.id.idstart);
        btnsdayend = findViewById(R.id.idend);
        txtdaystart = findViewById(R.id.txtdaystart);
        txtdayend = findViewById(R.id.txtdayend);
        lv = findViewById(R.id.lvorder);
        spinner = findViewById(R.id.spinner);
        btnBack = findViewById(R.id.btnBack);
    }

    private void ChooseDayStart() {
        btndaystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this, dateStartListener, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        dateStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, monthOfYear);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDate();
                updateOrderList();

            }

        };
    }

    private void ChooseDayEnd() {
        btnsdayend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(StatisticsActivity.this, dateEndListener, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });

        dateEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);

                // Kiểm tra xem ngày kết thúc có lớn hơn hoặc bằng ngày bắt đầu không
                if (selectedDate.getTimeInMillis() >= calendarStart.getTimeInMillis()) {
                    calendarEnd.set(Calendar.YEAR, year);
                    calendarEnd.set(Calendar.MONTH, monthOfYear);
                    calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateEndDate();

                } else {
                    // Hiển thị thông báo lỗi nếu ngày kết thúc không hợp lệ
                    Toast.makeText(StatisticsActivity.this, "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu", Toast.LENGTH_SHORT).show();
                }
                updateOrderList();
            }
        };
    }

    private void updateOrderList() {
        String start_day = txtdaystart.getText().toString();
        String end_day = txtdayend.getText().toString();

        orderDAO.getAllOrders(start_day, end_day, selectedDisplay, new OrderDataListener() {
            @Override
            public void onOrderListLoaded(ArrayList<Order> orderList) {

                OrderAdapter adapter = new OrderAdapter(StatisticsActivity.this, orderList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onNumberList(ArrayList<Integer> yearList) {

            }
        });
    }


    private void updateStartDate() {
        txtdaystart.setText(dateFormat.format(calendarStart.getTime()));
    }

    private void updateEndDate() {
        txtdayend.setText(dateFormat.format(calendarEnd.getTime()));
    }

//    private void processCopy() {
////private app
//        File dbFile = getDatabasePath(DATABASE_NAME);
//        if (!dbFile.exists()) {
//            try {
//                CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder",
//                        Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private String getDatabasePath() {
//        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
//    }
//
//    public void CopyDataBaseFromAsset() {
//// TODO Auto-generated method stub
//        try {
//            InputStream myInput;
//            myInput = getAssets().open(DATABASE_NAME);
//// Path to the just created empty db
//            String outFileName = getDatabasePath();
//// if the path doesn't exist first, create it
//            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
//            if (!f.exists())
//                f.mkdir();
//// Open the empty db as the output stream
//            OutputStream myOutput = new FileOutputStream(outFileName);
//// transfer bytes from the inputfile to the outputfile
//// Truyền bytes dữ liệu từ input đến output
//            int size = myInput.available();
//            byte[] buffer = new byte[size];
//            myInput.read(buffer);
//            myOutput.write(buffer);
//// Close the streams
//            myOutput.flush();
//            myOutput.close();
//            myInput.close();
//        } catch (IOException e) {
//// TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
