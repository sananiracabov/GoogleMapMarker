package com.googlemapmarker;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLoadedCallback {

    private GoogleMap map;
    private Spinner spinner;
    private MarkerOptions options = new MarkerOptions();
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        init();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeMapType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void init() {
        db = new DatabaseHandler(this);

        String[] array = {"Normal", "Hybrid", "Satellite", "Terrain"};
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void changeMapType(int i) {
        switch (i) {
            case 0:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 2:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 3:
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        map.setOnMapClickListener(this);
    }

    @Override
    public void onMapLoaded() {
        ArrayList<Model> markers = db.getList();

        for (int i = 0; i < markers.size(); i++) {
            double lat = markers.get(i).getLat();
            double lon = markers.get(i).getLon();
            options.position(new LatLng(lat, lon));
            map.addMarker(options);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Location location = new Location("Location");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
        options.position(l);
        map.addMarker(options);
        db.addLatAndLong(location.getLatitude(), location.getLongitude());
    }
}
