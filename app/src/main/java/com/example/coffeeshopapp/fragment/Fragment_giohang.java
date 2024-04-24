package com.example.coffeeshopapp.fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.activity.Bottom_nav;
import com.example.coffeeshopapp.activity.CartActivity;
import com.example.coffeeshopapp.activity.Customer_ListCouPonActivity;
import com.example.coffeeshopapp.activity.SuccessfulOrder;
import com.example.coffeeshopapp.activity.UpdateAddress;
import com.example.coffeeshopapp.adapter.CartItemAdapter;
import com.example.coffeeshopapp.databinding.FragmentGiohangBinding;
import com.example.coffeeshopapp.model.Cart;
import com.example.coffeeshopapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_giohang#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_giohang extends Fragment implements CartItemAdapter.OnDeleteItemClickListener,
        CartItemAdapter.OnQuantityChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int REQUEST_CHANGE_ADDRESS = 1;

    private static final Double phivanchuyen = 10000.0;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private CartItemAdapter cartItemAdapter;
    private List<Cart> cartItemList;
    private FragmentGiohangBinding bd;

    public Fragment_giohang() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_mon.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_giohang newInstance(String param1, String param2) {
        Fragment_giohang fragment = new Fragment_giohang();
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
        bd = FragmentGiohangBinding.inflate(getLayoutInflater());
        return bd.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bd.recyclerViewlistcartproduct.setHasFixedSize(true);
        bd.recyclerViewlistcartproduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        cartItemList = new ArrayList<>();
        cartItemAdapter = new CartItemAdapter(cartItemList, getContext());
        bd.recyclerViewlistcartproduct.setAdapter(cartItemAdapter);
        if (cartItemAdapter != null) {
            bd.llDatHang.setVisibility(VISIBLE);
        } else {
            bd.llDatHang.setVisibility(GONE);
        }

        eventNhanMaGiamGia();
        fetchDataFromFirebase();
        layThongTinDiaChi();
        cartItemAdapter.setOnDeleteItemClickListener(this);
        cartItemAdapter.setOnQuantityChangeListener(this);
        setEven();
    }

    private void setEven() {
        // nút back
        bd.imgbackincart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Bottom_nav.class);
                startActivity(intent);
            }
        });
        // nút chọn mã giảm giá
        bd.btnSelectCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Customer_ListCouPonActivity.class);
                startActivity(intent);
            }
        });


        // nút áp mã giảm giá
        bd.btnCouPon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponPercent = bd.tvPhantramCoupon.getText().toString().trim();

                // Kiểm tra xem mã giảm giá có đúng không

                // Áp dụng mã giảm giá
                double totalPrice = Double.parseDouble(bd.tvpriceofcart.getText().toString()
                        .replace(" vnd", "").replace(",", ""));

                double giamgia = Double.parseDouble(couponPercent.replace(" %", ""));

                displayTotalPrice(totalPrice, giamgia);
                Toast.makeText(getContext(), "Áp dụng mã giảm giá thành công", Toast.LENGTH_SHORT).show();


            }
        });
        // nút đặt hàng bằng tiền mặt
        bd.btnPayofcartTienmat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SuccessfulOrder.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        // nút thay đổi địa chỉ
        bd.btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateAddress.class);
                startActivityForResult(intent, REQUEST_CHANGE_ADDRESS);
            }
        });
    }

    private void eventNhanMaGiamGia() {
        // nhận mã giảm giá từ list
        Intent intent1 = getActivity().getIntent();
        if (intent1 != null) {
            String couponCode = intent1.getStringExtra("COUPON_CODE");
            String discountPercent = intent1.getStringExtra("DISCOUNT_PERCENT");
            bd.tvCoupon.setText(couponCode);
            bd.tvPhantramCoupon.setText(discountPercent);
        }
    }

    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Cart cartItem : cartItemList) {
            // Tính giá tiền cho từng sản phẩm và cộng vào tổng giá tiền
            //String "100.000" -> 100000.0 rồi lấy giá nhân số lượng
            double cartItemPrice = Double.parseDouble(cartItem.getProduct().getPrice().replace(".", ""));
            int cartItemQuantity = Integer.parseInt(cartItem.getQuantity());

            totalPrice += cartItemPrice * cartItemQuantity;
        }
        // Hiển thị tổng giá tiền trên giao diện
        displayTotalPrice(totalPrice);
    }

    // Tạo một định dạng số
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @SuppressLint("SetTextI18n")
    private void displayTotalPrice(double totalPrice) {
        // Chuyển đổi tổng giá tiền sang định dạng số tiền Việt Nam
        String formattedPrice = decimalFormat.format(totalPrice);
        // Hiển thị tổng giá tiền trên giao diện khi chưa có phí vận chuyển
        bd.tvpriceofcart.setText(formattedPrice + " vnd");
        // tổng tiền khi cộng thêm phí vận chuyển
        String formattedPricetotal = decimalFormat.format(totalPrice + phivanchuyen);
        bd.tvpricetotalofcart.setText(formattedPricetotal + " vnd");
    }

    private void displayTotalPrice(double totalPrice, double percent) {
        // Chuyển đổi tổng giá tiền sang định dạng số tiền Việt Nam
        String formattedPrice = decimalFormat.format(totalPrice);
        // Hiển thị tổng giá tiền trên giao diện khi chưa có phí vận chuyển
        bd.tvpriceofcart.setText(formattedPrice + " vnd");
        // tổng tiền khi cộng thêm phí vận chuyển
        String formattedPricetotal = decimalFormat.format((totalPrice * ((100 - percent) / 100)) + phivanchuyen);
        bd.tvpricetotalofcart.setText(formattedPricetotal + " vnd");
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @Override
    public void onDeleteItemClick(int position) {
        Cart deleteItem = cartItemList.get(position);
        String productId = deleteItem.getProduct().getId(); // Lấy id sản phẩm
        //  final private DatabaseReference databaseReference = FirebaseDatabase.getInstance()
        //            .getReference("Customer").child("Customer123").child("Cart");
        DatabaseReference itemRef = databaseReference.child(productId);
        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Xoá thành công từ Firebase, cập nhật lại danh sách và tính lại tổng giá tiền
                    cartItemList.remove(position);
                    cartItemAdapter.notifyItemRemoved(position);
                    calculateTotalPrice();
                    Toast.makeText(getContext(), "Xoá sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xảy ra lỗi khi xoá từ Firebase
                    Toast.makeText(getContext(), "Lỗi khi xoá sản phẩm: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Lấy dữ liệu các sản phẩm trong giỏ hàng từ Firebase
    private void fetchDataFromFirebase() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        databaseReference.child("Customer").child(userId).child("Cart").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                // Kiểm tra xem nếu có dữ liệu trong giỏ hàng của khách hàng
                if (snapshot.exists()) {
                    // Duyệt qua tất cả các nút con (các sản phẩm) trong giỏ hàng
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        // Lấy thông tin về sản phẩm
                        Product product = productSnapshot.child("product").getValue(Product.class);
                        String quantity = productSnapshot.child("quantity").getValue(String.class);
                        String size = productSnapshot.child("size").getValue(String.class);
                        Cart cart = new Cart(product, quantity, size);
                        cartItemList.add(cart);
                    }
                    cartItemAdapter.notifyDataSetChanged();
                    calculateTotalPrice();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hiển thị thông báo lỗi
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage());
                Toast.makeText(getContext(), "Lỗi khi đọc dữ liệu từ Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }// end fetch data from firebase

    private void layThongTinDiaChi() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        // Lấy thông tin địa chỉ
        DatabaseReference customerInfoRef = FirebaseDatabase.getInstance().getReference("Customer")
                .child(userId);
        customerInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem có dữ liệu tồn tại không
                if (snapshot.exists()) {
                    // Lấy địa chỉ từ dữ liệu
                    String address = snapshot.child("address").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    //Set data cho textview
                    bd.tvAddressfc.setText(address);
                    bd.tvNameCusfc.setText(name);
                    bd.tvphonefc.setText(phone);
                } else {
                    bd.tvAddressfc.setText("Rỗng");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("FirebaseError", "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Lỗi khi đọc dữ liệu từ Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onIncreaseQuantity(int position) {
        Cart cartItem = cartItemList.get(position);
        int currentQuantity = Integer.parseInt(cartItem.getQuantity());
        currentQuantity++;
        cartItem.setQuantity(String.valueOf(currentQuantity));

        // Cập nhật số lượng sản phẩm trong Firebase
        DatabaseReference itemRef = databaseReference.child(cartItem.getProduct().getId()); // Giả sử có một trường id trong đối tượng Cart để định danh mỗi sản phẩm
        itemRef.child("quantity").setValue(String.valueOf(currentQuantity));

        // Cập nhật lại giao diện
        cartItemAdapter.notifyItemChanged(position);
        calculateTotalPrice();
    }

    @Override
    public void onDecreaseQuantity(int position) {
        Cart cartItem = cartItemList.get(position);
        int currentQuantity = Integer.parseInt(cartItem.getQuantity());
        if (currentQuantity > 1) {
            currentQuantity--;
            cartItem.setQuantity(String.valueOf(currentQuantity));

            // Cập nhật số lượng sản phẩm trong Firebase bằng id
            DatabaseReference itemRef = databaseReference.child(cartItem.getProduct().getId());
            itemRef.child("quantity").setValue(String.valueOf(currentQuantity));

            // Cập nhật lại giao diện
            cartItemAdapter.notifyItemChanged(position);
            calculateTotalPrice();
        }
    }
}