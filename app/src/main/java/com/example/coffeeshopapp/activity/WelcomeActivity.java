package com.example.coffeeshopapp.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshopapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WelcomeActivity extends AppCompatActivity {
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="DatabaseAppCoffee.db";
    Button btnKhamPha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        processCopy();
        setControl();
        setEven();

    }

    protected void setControl(){
        btnKhamPha = findViewById(R.id.btnKhamPha);
    }
    protected void setEven(){
        btnKhamPha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private void processCopy() {
//private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;}
    public void CopyDataBaseFromAsset() {
// TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
// Path to the just created empty db
            String outFileName = getDatabasePath();
// if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
// Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
// transfer bytes from the inputfile to the outputfile
// Truyền bytes dữ liệu từ input đến output
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
// Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}