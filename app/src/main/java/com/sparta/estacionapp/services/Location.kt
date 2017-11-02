package com.sparta.estacionapp.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.sparta.estacionapp.models.Garage


class Location : Service() {

    private lateinit var locationManager: LocationManager
    private val minimumDistance = 500000f

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
        var location = garage.latLng()
        val intent = Intent(PROXIMITY_ACTION)
        applicationContext.sendBroadcast(intent)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        locationManager.addProximityAlert(location.latitude, location.longitude, minimumDistance, -1, pendingIntent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return serviceBinder
    }

    companion object {
        val PROXIMITY_ACTION = "com.sparta.estacionapp.PROXIMITY_ACTION"
    }
}
