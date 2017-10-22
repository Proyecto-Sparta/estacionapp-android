package com.sparta.estacionapp.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.rest.DriverService


/**
 * A simple [Fragment] subclass.
 */
class Search : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var seekRadio: SeekBar
    private lateinit var seekBarValue : TextView

    private lateinit var placeAutocompleteFragment: SupportPlaceAutocompleteFragment
    private lateinit var fragment : View

    private var circleRadius : Circle? = null
    private var markers : MutableMap<Marker, Garage?> = mutableMapOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fragment = inflater!!.inflate(R.layout.fragment_search, container, false)

        initSeekBarRadio(fragment)
        initPlaceAutocompleteFragment()

//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message")
//        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot?) {
//               var a = p0!!.getValue(String::class.java)
//            }
//
//        })
//
//        myRef.setValue("Hello, World!")

        //search.setOnClickListener { _ -> requestGarage() }
        return fragment
    }

//    private fun requestGarage() {
//        val socket = Socket("ws://localhost:4000/socket")
//        socket.connect()
//
//        val channel = socket.chan("garage:foo", null)
//        channel.join()
//    }

    private fun initSeekBarRadio(fragment: View) {
        seekBarValue = fragment.findViewById(R.id.txt_radio)

        seekRadio = fragment.findViewById(R.id.seek_radio)
        seekRadio.max = 14
        seekRadio.progress = 9

        seekBarValue.text = getRadioText(seekRadio.progress)

        seekRadio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBarValue.text = getRadioText(progress)
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun getRadioText(progress: Int) = "${getRadio(progress)}m"

    fun getRadio(progress : Int): Int {
        return (progress + 1) * 100
    }

    private fun initPlaceAutocompleteFragment() {
        placeAutocompleteFragment = childFragmentManager.findFragmentById(R.id.search_fragment) as SupportPlaceAutocompleteFragment
        placeAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                DriverService(activity).searchGarage(
                        place.latLng.latitude,
                        place.latLng.longitude,
                        getRadio(seekRadio.progress),
                        { garages -> updateMarker(place.latLng, garages) }
                )
            }

            override fun onError(status: Status) {
                Log.e(TAG, "An error occurred: " + status)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        setupMap()
    }

    private fun setupMap() {
        mapView = fragment.findViewById(R.id.map_view)
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.getMapAsync { this.onMapReadyCallback(it) }
    }

    private fun requestLocationPermission() {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ActivityCompat.requestPermissions(activity, Search.REQUIRED_PERMISSIONS, Search.REQUEST_CODE)
        }
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null)
    }



    private fun updateMarker(latLng: LatLng, garages : List<Garage> = listOf()) {
        clearMarkers()
        createCircle(latLng)
        addCenterMarker(latLng)
        createGarageMarkers(garages)
    }

    private fun addCenterMarker(latLng: LatLng) {
        val marker = googleMap.addMarker(MarkerOptions().position(latLng))
        markers.put(marker, null)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circleRadius!!)))
    }

    private fun getZoomLevel(circle: Circle): Float {
        val radius = circle.radius + circle.radius / 2
        val scale = radius / 500
        return (16 - Math.log(scale) / Math.log(2.0)).toFloat()
    }

    private fun createGarageMarkers(garages: List<Garage>) {
        garages.forEach { garage ->
            val location = garage.latLng()
            val markerOption = MarkerOptions()
                    .position(location)
                    .title(garage.name)
                    .icon(getBitmapDescriptor())
            val marker = googleMap.addMarker(markerOption)
            markers.put(marker, garage)
        }
    }

    private fun clearMarkers() {
        markers.forEach { it.key.remove() }
        markers.clear()
    }

    @SuppressLint("NewApi")
    private fun createCircle(latLng : LatLng) {
        if (circleRadius != null) circleRadius!!.remove()
        val color = resources.getColor(R.color.colorAccent, activity.theme)
        val meters = getRadio(seekRadio.progress).toDouble()
        val options = CircleOptions()
                .center(latLng)
                .radius(meters)
                .strokeWidth(5f)
                .fillColor(withOpacity(color))
                .strokeColor(color)
        circleRadius = googleMap.addCircle(options)
    }

    private fun withOpacity(color: Int) = color.and(0x00FFFFFF).or(0x25000000)

    private fun getBitmapDescriptor(): BitmapDescriptor {
        return BitmapDescriptorFactory.fromResource(R.mipmap.ic_cone)
    }

    private fun onMapReadyCallback(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener { showMarkerInfo(it) }
        requestLocationPermission()
    }

    private fun showMarkerInfo(marker: Marker?): Boolean {
        markers.keys.forEach { it.hideInfoWindow() }
        val garage = markers.getOrDefault(marker!!, null)
        if (garage != null) {
            if (marker.isInfoWindowShown) {
                marker.showInfoWindow()
            } else {
                marker.hideInfoWindow()
            }
        }
        return false
    }

    private val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            updateMarker(LatLng(location.latitude, location.longitude))
        }

        override fun onProviderDisabled(p0: String?) {}
        override fun onProviderEnabled(p0: String?) {}
        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private val REQUEST_CODE = 9000
    }

}