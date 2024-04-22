package com.example.coffeeshopapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeeshopapp.R;

public class WelcomeActivity extends AppCompatActivity {

    Button btnKhamPha;
     /* String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="DatabaseAppCoffee.db";*/
    // SQLiteDatabase database=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

     //  processCopy();
       /*   List<Product> productList = new ArrayList<>();
        // Tạo các đối tượng sản phẩm và thêm vào danh sách
        Product product1 = new Product();
        product1.setName("Cafe americano");
        product1.setImage("https://firebasestorage.googleapis.com/v0/b/coffeeapp-c1842.appspot.com/o/caffe-americano_tcm89-2059_w1024_n.jpg?alt=media&token=bc139422-4122-4f1c-8eb5-1e763afc50fe");
        product1.setPrice("5000.0");
        productList.add(product1);

        Product product2 = new Product();
        product2.setName("Cafe Latte");
        product2.setImage("https://firebasestorage.googleapis.com/v0/b/coffeeapp-c1842.appspot.com/o/caffee-latte_tcm89-2062_w1024_n.jpg?alt=media&token=92f2dda5-47e6-4fbd-b61d-36ac16604054");
        product2.setPrice("10000");
        productList.add(product2);
        // Tạo một đối tượng DAO
        ProductDAO productDAO = new ProductDAO(this); // context là đối tượng Context của ứng dụng
        // Thêm các sản phẩm từ danh sách vào cơ sở dữ liệu bằng cách sử dụng vòng lặp
        for (Product product : productList) {
            productDAO.add(product);
        }*/
        //database=openOrCreateDatabase("DatabaseAppCoffee.db",MODE_PRIVATE,null);
        setControl();
       // setEven();
    }

    protected void setControl() {
        btnKhamPha = findViewById(R.id.btnKhamPha);
    }

    protected void setEven() {
        btnKhamPha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
       /* // Lấy danh sách các tệp hình ảnh trong thư mục
        File directory = new File("C:\\Users\\Legion 5\\Desktop\\android\\imgcoffee\\");
        File[] files = directory.listFiles();

        // Kiểm tra xem files có rỗng không
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Đọc hình ảnh từ tệp
                    byte[] imageBytes = new byte[(int) file.length()];
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        fis.read(imageBytes);
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Lấy tên sản phẩm từ tên tệp
                    String productName = file.getName().replace(".jpg", "").replace("_", " ");
                    double productPrice = 0; // Cần cập nhật giá sản phẩm tương ứng

                    // Thêm sản phẩm vào cơ sở dữ liệu
                    ProductDAO dbHelper = new ProductDAO(WelcomeActivity.this); // Thay YourDbHelper bằng tên lớp của bạn
                    long rowId = addProductToDatabase(productName, productPrice, imageBytes, dbHelper);
                    if (rowId != -1) {
                        Toast.makeText(WelcomeActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WelcomeActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    } // đóng của setevent





    /*private void processCopy() {
    //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

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
    }}
*/