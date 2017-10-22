package com.sparta.estacionapp.fragments

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.rest.DriverService
import kotlinx.android.synthetic.main.fragment_search.*

class Search : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private lateinit var seekRadio: SeekBar
    private lateinit var seekBarValue : TextView

    private lateinit var placeAutocompleteFragment: SupportPlaceAutocompleteFragment
    private lateinit var fragment : View

    private lateinit var slidingView: SlidingUpPanelLayout
    private lateinit var garageName : TextView
    private lateinit var garageEmail : TextView

    private var currentPositionEntry: PositionEntry<LatLng, Marker>? = null
    private var circleRadius : Circle? = null
    private var markers : MutableMap<Marker, Garage> = mutableMapOf()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        fragment = inflater!!.inflate(R.layout.fragment_search, container, false)

        initGarageDetails(fragment)
        initSeekBarRadio(fragment)
        initPlaceAutocompleteFragment()

        return fragment
    }

    private fun getRadio(progress : Int) = (progress + 1) * 100
    private fun getRadioText(progress: Int) = "${getRadio(progress)}m"

    private fun initGarageDetails(fragment: View) {
        slidingView = fragment.findViewById(R.id.sliding_layout)
        garageName = fragment.findViewById(R.id.garage_name)
        garageEmail = fragment.findViewById(R.id.garage_email)

        slidingView.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
    }

    private fun initSeekBarRadio(fragment: View) {
        seekRadio = fragment.findViewById(R.id.seek_radio)
        seekRadio.max = 14
        seekRadio.progress = 9
        seekRadio.setOnSeekBarChangeListener(seekBarChangeListener)

        seekBarValue = fragment.findViewById(R.id.txt_radio)
        seekBarValue.text = getRadioText(seekRadio.progress)
    }

    private fun initPlaceAutocompleteFragment() {
        placeAutocompleteFragment = childFragmentManager.findFragmentById(R.id.search_fragment) as SupportPlaceAutocompleteFragment
        placeAutocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener)
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
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
    }

    private fun updateMarker(latLng: LatLng, garages : List<Garage> = listOf()) {
        clearMarkers()
        createCircle(latLng)
        addCenterMarker(latLng)
        createGarageMarkers(garages)
    }

    private fun addCenterMarker(latLng: LatLng) {
        if (currentPositionEntry != null) {
            currentPositionEntry!!.value.remove()
        }
        val marker = googleMap.addMarker(MarkerOptions().position(latLng))
        currentPositionEntry = PositionEntry(latLng, marker)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circleRadius!!)))
    }

    private fun getZoomLevel(circle: Circle): Float {
        val radius = circle.radius + circle.radius / 2
        val scale = radius / 500
        return (16 - Math.log(scale) / Math.log(2.0)).toFloat()
    }

    private fun createGarageMarkers(garages: Collection<Garage>) {
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

    private fun createCircle(latLng : LatLng) {
        if (circleRadius != null) circleRadius!!.remove()
        val color = getColor()
        val meters = getRadio(seekRadio.progress).toDouble()
        val options = CircleOptions()
                .center(latLng)
                .radius(meters)
                .strokeWidth(5f)
                .fillColor(withOpacity(color))
                .strokeColor(color)
        circleRadius = googleMap.addCircle(options)
    }

    private fun getColor(): Int {
        val color = ResourcesCompat.getColor(resources, R.color.colorAccent, activity.theme)
        val r = (color shr 16) and 0xFF
        val g = (color shr  8) and 0xFF
        val b = (color shr  0) and 0xFF
        return Color.rgb(r, g, b)
    }

    private fun withOpacity(color: Int) = color.and(0x00FFFFFF).or(0x25000000)

    private fun getBitmapDescriptor(): BitmapDescriptor {
        return BitmapDescriptorFactory.fromResource(R.mipmap.ic_cone)
    }

    private fun onMapReadyCallback(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnMarkerClickListener { openGarageDetails(it) }
        googleMap.setOnMapLoadedCallback { requestLocationPermission() }
        searchGarages(LatLng(-34.585000, -58.414000))
    }

    private fun openGarageDetails(marker: Marker?): Boolean {
        if (marker == currentPositionEntry!!.value) {
            slidingView.panelState = SlidingUpPanelLayout.PanelState.HIDDEN
            return false
        }
        val garage = markers.getOrDefault(marker!!, Garage.stub())
        slidingView.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        garage_name.text = garage.name
        garage_email.text = garage.email
        return true
    }

    // ********************
    // Listeners
    // ********************

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (currentPositionEntry == null) {
                updateMarker(LatLng(location.latitude, location.longitude))
            }
        }

        override fun onProviderDisabled(p0: String?) {}
        override fun onProviderEnabled(p0: String?) {}
        override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    }

    private val placeSelectionListener: PlaceSelectionListener = object : PlaceSelectionListener {
        override fun onPlaceSelected(place: Place) {
            searchGarages(place.latLng)
        }

        override fun onError(status: Status) {
            Log.e(TAG, "An error occurred: " + status)
        }
    }

    private fun searchGarages(latLng: LatLng) {
        DriverService(activity).searchGarage(
                latLng.latitude,
                latLng.longitude,
                getRadio(seekRadio.progress),
                { garages -> updateMarker(latLng, garages) }
        )
    }

    private val seekBarChangeListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            seekBarValue.text = getRadioText(progress)
            if (currentPositionEntry != null) searchGarages(currentPositionEntry!!.key)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {}
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
    }

    // ********************
    // Companion object
    // ********************

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private val REQUEST_CODE = 9000
    }


    class PositionEntry<out K, out V>(override val key: K,
                                      override val value: V) : kotlin.collections.Map.Entry<K, V>
}