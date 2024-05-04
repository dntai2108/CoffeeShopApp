package com.example.coffeeshopapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityGiaoHangBinding;
import com.example.coffeeshopapp.model.Order;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GiaoHang extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_CODE = 1;
    private LocationManager locationManager;
    private Geocoder geocoder;
    private ActivityGiaoHangBinding bd;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<Order> listOrder = new ArrayList<>();
    private String latidudeShipper;
    private String longituteShipper;
    private List<Polyline> polylines = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityGiaoHangBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
        } else {
            // Khởi tạo LocationManager và lấy vị trí
            getLocation();
        }
        getLocation();
        geocoder = new Geocoder(this, Locale.getDefault());
        bd.mapView.setTileSource(TileSourceFactory.MAPNIK); // Sử dụng tile source của OpenStreetMap
        bd.mapView.getController().setCenter(new GeoPoint(10.7769, 106.7009)); // Ví dụ: TP.HCM
        bd.mapView.getController().setZoom(15); // Mức zoom
        bd.mapView.setBuiltInZoomControls(true);
        bd.mapView.setMultiTouchControls(false);
        setControl();
        setEvent();
    }

    private void setControl() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        DatabaseReference customerRef = databaseReference.child("Customer");
        databaseReference.child("Customer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot orderSnapshot : snapshot.child("Order").getChildren()){
                        String shiperId = orderSnapshot.child("shipperId").getValue(String.class);
                        if(shiperId!=null){
                            if(shiperId.equals(userId)){
                                String latitude = orderSnapshot.child("latitude").getValue(String.class);
                                String longitude = orderSnapshot.child("longitude").getValue(String.class);
                                String orderId = orderSnapshot.child("orderId").getValue(String.class);
                                String status = orderSnapshot.child("status").getValue(String.class);
                                if(status.equals("Đang giao")){
                                    Order o = new Order();
                                    o.setLongitude(longitude);
                                    o.setLatitude(latitude);
                                    o.setOrderId(orderId);
                                    listOrder.add(o);
                                }
                            }
                        }
                    }
                }
                for (Order o : listOrder){
                    Marker marker = new Marker(bd.mapView);
                    marker.setIcon(getResources().getDrawable(R.drawable.baseline_add_location_24));
                    marker.setTitle(o.getOrderId());
                    GeoPoint location = new GeoPoint(Double.parseDouble(o.getLatitude()),Double.parseDouble(o.getLongitude()));
                    marker.setPosition(location);
                    bd.mapView.getOverlays().add(marker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setEvent() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        Marker shipperMarker = new Marker(bd.mapView);
        bd.mapView.getOverlays().add(shipperMarker);
        Handler handler = new Handler();
        Runnable updateShipperLocationRunnable = new Runnable() {
            @Override
            public void run() {
                getLocation();
                // Khi shipper di chuyển và bạn muốn cập nhật vị trí của marker
                databaseReference.child("shipper_location").child(userId).child("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        latidudeShipper = snapshot.getValue(String.class);
                        databaseReference.child("shipper_location").child(userId).child("longitude").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                longituteShipper =  snapshot.getValue(String.class);
                                if(latidudeShipper == null || latidudeShipper.isEmpty() || longituteShipper.isEmpty() || longituteShipper == null){
                                    return;
                                }
                                if(polylines.size() != 0){
                                    bd.mapView.getOverlayManager().remove(polylines);
                                    polylines.clear();
                                }
                                for (Order o : listOrder) {
                                    List<GeoPoint> points = new ArrayList<>();
                                    GeoPoint shipperLocation = new GeoPoint(Double.parseDouble(latidudeShipper),Double.parseDouble(longituteShipper));
                                    GeoPoint location = new GeoPoint(Double.parseDouble(o.getLatitude()),Double.parseDouble(o.getLongitude()));
                                    points.add(shipperLocation); // Vị trí của shipper
                                    points.add(location); // Vị trí của customer

                                    Polyline polyline = new Polyline();
                                    polyline.setPoints(points);
                                    bd.mapView.getOverlayManager().add(polyline);

                                    // Thêm Polyline vào danh sách
                                    polylines.add(polyline);
                                }
                                // Tạo một đối tượng GeoPoint mới với vị trí mới của shipper
                                GeoPoint shipperLocation = new GeoPoint(Double.parseDouble(latidudeShipper),Double.parseDouble(longituteShipper));
                                shipperMarker.setIcon(getResources().getDrawable(R.drawable.baseline_account_circle_24)); // Sử dụng icon tùy chỉnh nếu cần
                                shipperMarker.setTitle("Shipper");
                                // Cập nhật vị trí mới cho marker của shipper
                                shipperMarker.setPosition(shipperLocation);
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

    private void getLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        }
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                // Sử dụng tọa độ latitude và longitude ở đây
                                // Ví dụ: log ra console
                                databaseReference.child("shipper_location").child(userId).child("latitude").setValue(String.valueOf(latitude));
                                databaseReference.child("shipper_location").child(userId).child("longitude").setValue(String.valueOf(longitude));
                            }
                        }
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        GeoPoint currentLocation = new GeoPoint(latitude, longitude);
//        bd.mapView.getController().animateTo(currentLocation);
//        bd.mapView.getController().setZoom(17);
        databaseReference.child("shipper_location").child(userId).child("latitude").setValue(String.valueOf(latitude));
        databaseReference.child("shipper_location").child(userId).child("longitude").setValue(String.valueOf(longitude));
        Toast.makeText(this, "Zoomed to current location", Toast.LENGTH_SHORT).show();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

}