package com.sparta.estacionapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressWarnings("FieldCanBeLocal")
public class EstacionAppActivity extends AppCompatActivity {

    private Marker myPosition;
    private MapView mapView;
    private GoogleMap googleMap;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private final int REQUEST_CODE = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacion_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupDrawer(toolbar);
        setupNavigation();
        setupMap();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void setupNavigation() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(getOnNavigationItemSelectedListener());
    }

    private void setupDrawer(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = setupToggle(toolbar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupMap() {
        requestLocationPermission();
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(new Bundle());
        mapView.onStart();
        mapView.getMapAsync(getOnMapReadyCallback());
    }

    private void requestLocationPermission() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 0, getListener());

    }

    private ActionBarDrawerToggle setupToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private void updateMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (myPosition != null) {
            myPosition.setPosition(latLng);
        } else {
            myPosition = googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }

    @NonNull
    private OnMapReadyCallback getOnMapReadyCallback() {
        return map -> {
            googleMap = map;
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        };
    }

    @NonNull
    private NavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return (item) -> {

            int id = item.getItemId();

            if (id == R.id.nav_camera) return true;
            else if (id == R.id.nav_send) return true;
            else if (id == R.id.nav_share) return true;
            else if (id == R.id.nav_manage) return true;
            else if (id == R.id.nav_gallery) return true;
            else if (id == R.id.nav_slideshow) return true;

            closeDrawer();

            return true;
        };
    }

    @NonNull
    private LocationListener getListener() {
        return new LocationListener() {
            @Override public void onLocationChanged(Location location) { updateMarker(location); }
            @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override public void onProviderEnabled(String s) {}
            @Override public void onProviderDisabled(String s) {}
        };
    }

}
