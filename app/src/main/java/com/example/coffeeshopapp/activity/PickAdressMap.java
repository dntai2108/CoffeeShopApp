package com.example.coffeeshopapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.coffeeshopapp.R;
import com.example.coffeeshopapp.databinding.ActivityPickAdressMapBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PickAdressMap extends AppCompatActivity {
    private Geocoder geocoder;
    private ActivityPickAdressMapBinding bd;
    private LocationManager locationManager;
    private Marker custome;
    private String Address = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = ActivityPickAdressMapBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this, Locale.getDefault());
        bd.mapView.setTileSource(TileSourceFactory.MAPNIK);
        bd.mapView.getController().setCenter(new GeoPoint(10.7769, 106.7009)); // Ví dụ: TP.HCM
        bd.mapView.getController().setZoom(15); // Mức zoom
        bd.mapView.setBuiltInZoomControls(true);
        bd.mapView.setMultiTouchControls(false);
        setEvent();
    }

    private void setEvent() {
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                // Lấy địa chỉ khi người dùng click vào bản đồ
                for (Overlay overlay : bd.mapView.getOverlays()) {
                    if (overlay instanceof Marker && ((Marker) overlay).getTitle().equals("Địa điểm")) {
                        bd.mapView.getOverlays().remove(overlay);
                    }
                }
                custome = new Marker(bd.mapView);
                custome.setPosition(geoPoint);
                custome.setIcon(getResources().getDrawable(R.drawable.baseline_add_location_24));
                custome.setTitle("Địa điểm");
                bd.mapView.getOverlays().add(custome);
                getAddressFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude());
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false;
            }
        };

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        bd.mapView.getOverlays().add(0, mapEventsOverlay);
        bd.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickAdressMap.this,UpdateAddress.class);
                GeoPoint geoPoint = custome.getPosition();
                String Latitude = String.valueOf(geoPoint.getLatitude());
                String Longitude = String.valueOf(geoPoint.getLongitude());
                intent.putExtra("MapToAddress",Address);
                intent.putExtra("Latitude",Latitude);
                intent.putExtra("Longitude",Longitude);
                startActivity(intent);
                finish();
            }
        });
        bd.ivQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickAdressMap.this,UpdateAddress.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void getAddressFromLocation(double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                Address = addressLine;
                Toast.makeText(this, "Selected Address: " + addressLine, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No address found for this location", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}