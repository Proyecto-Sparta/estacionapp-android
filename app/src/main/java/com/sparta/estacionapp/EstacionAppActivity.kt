package com.sparta.estacionapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class EstacionAppActivity : AppCompatActivity() {

    private var myPosition: Marker? = null
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var drawer: DrawerLayout

    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estacion_app)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setupDrawer(toolbar)
        setupNavigation()
        setupMap()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupNavigation() {
        navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener { this.onNavigationMenuItemSelected(it) }
    }

    private fun setupDrawer(toolbar: Toolbar) {
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = setupToggle(toolbar)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupMap() {
        requestLocationPermission()
        mapView = findViewById(R.id.map_view) as MapView
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.getMapAsync { this.onMapReadyCallback(it) }
    }

    private fun requestLocationPermission() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 0f, listener)
    }

    private fun setupToggle(toolbar: Toolbar): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    }

    private fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    private fun updateMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        if (myPosition != null) {
            myPosition!!.position = latLng
        } else {
            myPosition = googleMap.addMarker(MarkerOptions().position(latLng))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16f))
        }
    }

    private fun onMapReadyCallback(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun onNavigationMenuItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_profile, R.id.nav_log_out, R.id.nav_search -> return true
        }

        closeDrawer()

        return true
    }

    private val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            updateMarker(location)
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
        override fun onProviderEnabled(s: String) {}
        override fun onProviderDisabled(s: String) {}
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private val REQUEST_CODE = 9000
    }

}
