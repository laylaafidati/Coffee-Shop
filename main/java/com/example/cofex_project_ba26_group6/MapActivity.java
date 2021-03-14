package com.example.cofex_project_ba26_group6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    String cafeName;
    Double LAT, LNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        cafeName = getIntent().getStringExtra("cafeName");
        LAT = Double.valueOf(getIntent().getStringExtra("LAT"));
        LNG = Double.valueOf(getIntent().getStringExtra("LNG"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_CafeMaps);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        LatLng cafeLocation = new LatLng(LAT, LNG);

        map.addMarker(new MarkerOptions().position(cafeLocation).title(cafeName));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(cafeLocation, 16f));
    }
}