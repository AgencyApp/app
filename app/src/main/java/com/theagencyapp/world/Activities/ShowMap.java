package com.theagencyapp.world.Activities;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.MapView;

import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.theagencyapp.world.R;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;


/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * <p>
 * requires forwarding all the important lifecycle methods onto MapView.
 */

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback {


    private MapView mMapView;
    Double Latitude;
    Double Longitude;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        Latitude = getIntent().getDoubleExtra("lat", 0);
        Longitude = getIntent().getDoubleExtra("lng", 0);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

    }


    @Override

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }


        mMapView.onSaveInstanceState(mapViewBundle);

    }


    @Override

    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }


    @Override

    protected void onStart() {
        super.onStart();
        mMapView.onStart();

    }


    @Override

    protected void onStop() {
        super.onStop();
        mMapView.onStop();

    }


    @Override

    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title("Marker"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude, Longitude), 10);

        map.animateCamera(cameraUpdate);
    }


    @Override

    protected void onPause() {
        mMapView.onPause();
        super.onPause();

    }


    @Override

    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

    }


    @Override

    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();

    }


}