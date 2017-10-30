package com.sparta.estacionapp.models

import android.app.Activity
import android.content.Intent
import android.net.Uri

class MapNavigation(private val activity: Activity) {

    fun navigateTo(garage: Garage) {
        val latLng = garage.latLng()
        val gmmIntentUri = Uri.parse("google.navigation:q=${latLng.latitude},${latLng.longitude}&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.`package` = "com.google.android.apps.maps"
        activity.startActivity(mapIntent)
    }
}

