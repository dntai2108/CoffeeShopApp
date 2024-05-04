package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityFolowOrderBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FolowOrder extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Marker custome;
    private Geocoder geocoder;
    private String maDonHang;
    private ActivityFolowOrderBinding bd;
    private String longitudeCustomer;
    private String latitudeCustomer;
    private String longitudeShipper;
    private String latitudeShipper;
    private String shipperId;
    private Polyline polyline;
    GeoPoint customerLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityFolowOrderBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        setControl();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        bd.mapView.setTileSource(TileSourceFactory.MAPNIK); // Sử dụng tile source của OpenStreetMap
        bd.mapView.getController().setCenter(new GeoPoint(10.7769, 106.7009)); // Ví dụ: TP.HCM
        bd.mapView.getController().setZoom(15); // Mức zoom
        bd.mapView.setBuiltInZoomControls(true);
        bd.mapView.setMultiTouchControls(false);
        setEvent();
    }

    private void setEvent() {
        Marker shipperMarker = new Marker(bd.mapView);
        shipperMarker.setIcon(getResources().getDrawable(R.drawable.baseline_account_circle_24)); // Sử dụng icon tùy chỉnh nếu cần
        shipperMarker.setTitle("Shipper");
        bd.mapView.getOverlays().add(shipperMarker);
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Customer").child(userId).child("Order").child(maDonHang);
        databaseReference.child("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                latitudeCustomer = snapshot.getValue(String.class);
                databaseReference.child("longitude").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        longitudeCustomer = snapshot.getValue(String.class);
                        Marker customerMarker = new Marker(bd.mapView);
                        customerLocation = new GeoPoint(Double.parseDouble(latitudeCustomer), Double.parseDouble(longitudeCustomer));
                        customerMarker.setPosition(customerLocation);
                        customerMarker.setIcon(getResources().getDrawable(R.drawable.baseline_add_location_24)); // Sử dụng icon tùy chỉnh nếu cần
                        customerMarker.setTitle("Khách hàng");
                        bd.mapView.getOverlays().add(customerMarker);
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
        Handler handler = new Handler();
        Runnable updateShipperLocationRunnable = new Runnable() {
            @Override
            public void run() {

                databaseReference.child("shipperId").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        shipperId = snapshot.getValue(String.class);
                        FirebaseDatabase.getInstance().getReference().child("shipper_location").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    latitudeShipper =  snapshot.child(shipperId).child("latitude").getValue(String.class);
                                    longitudeShipper = snapshot.child(shipperId).child("longitude").getValue(String.class);
                                    GeoPoint shipperLocation = new GeoPoint(Double.parseDouble(latitudeShipper),Double.parseDouble(longitudeShipper));
                                    shipperMarker.setPosition(shipperLocation);
                                    bd.mapView.getOverlayManager().remove(polyline);
                                    Polyline newPolyline = new Polyline();
                                    newPolyline.setWidth(5);
                                    newPolyline.setColor(Color.RED);
                                    newPolyline.addPoint(customerLocation);
                                    newPolyline.addPoint(shipperLocation);
                                    polyline = newPolyline;
                                    bd.mapView.getOverlays().add(polyline);
                                    bd.mapView.invalidate();
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
                // Lập lịch chạy lại Runnable sau 10 giây
                handler.postDelayed(this, 10000); // 10000 milliseconds = 10 giây
            }
        };
        handler.post(updateShipperLocationRunnable);
        bd.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setControl() {
        maDonHang = getIntent().getStringExtra("maDonHang");
        String a = "Sadasd";
    }
}