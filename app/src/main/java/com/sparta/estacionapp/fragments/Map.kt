package com.sparta.estacionapp.fragments


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sparta.estacionapp.R


class Map : Fragment() {

    private var myPosition: Marker? = null
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_map, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupMap()
    }

    private fun setupMap() {
        requestLocationPermission()
        mapView = view!!.findViewById<MapView>(R.id.map_view)
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.getMapAsync { this.onMapReadyCallback(it) }
    }

    private fun requestLocationPermission() {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_CODE)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 250, 0f, listener)
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
