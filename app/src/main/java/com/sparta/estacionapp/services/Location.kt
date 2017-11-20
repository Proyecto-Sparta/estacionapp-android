package com.sparta.estacionapp.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.sparta.estacionapp.activities.Home
import com.sparta.estacionapp.models.Garage


class Location : Service() {

    private lateinit var locationManager: LocationManager

    private val serviceBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        val service: Location
            get() = this@Location
    }

    override fun onCreate() {
        super.onCreate()
        initLocationListener()
    }

    private fun initLocationListener() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    fun setProximityAlert(garage: Garage, context: Context) {

        val intent = Intent(PROXIMITY_ACTION)
        val filter = IntentFilter(PROXIMITY_ACTION)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val location = garage.latLng()

        registerReceiver(ProximityListener(pendingIntent, context, locationManager, location), filter)

        locationManager.addProximityAlert(location.latitude, location.longitude, DISTANCE, -1, pendingIntent)
    }

    override fun onBind(intent: Intent): IBinder? = serviceBinder

    companion object {
        val PROXIMITY_ACTION = "com.sparta.estacionapp.PROXIMITY_ACTION"
        var DISTANCE = 100f
    }

    class ProximityListener(val pendingIntent: PendingIntent,
                            val context: Context,
                            val locationManager: LocationManager,
                            val location: LatLng) : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val key = LocationManager.KEY_PROXIMITY_ENTERING
            val entering = intent!!.getBooleanExtra(key, false)

            if (entering) {
                val bringToForegroundIntent = Intent(context, Home::class.java)
                bringToForegroundIntent.flags = Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

                ContextCompat.startActivity(context, bringToForegroundIntent, null)
                locationManager.removeProximityAlert(pendingIntent)
            } else {
                locationManager.removeProximityAlert(pendingIntent)
                locationManager.addProximityAlert(location.latitude, location.longitude, DISTANCE, -1, pendingIntent)
            }
        }
    }
}
