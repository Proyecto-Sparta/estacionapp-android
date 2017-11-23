package com.sparta.estacionapp.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.models.MapNavigation
import com.sparta.estacionapp.models.responses.DriverResponse
import com.sparta.estacionapp.services.Constants
import com.sparta.estacionapp.services.Location

class Navigation : Fragment() {

    private lateinit var location: Location
    private lateinit var garage: Garage
    private lateinit var driverResponse: DriverResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        garage = arguments.getSerializable(Constants.CURRENT_GARAGE) as Garage
        driverResponse= arguments.getSerializable(Constants.DRIVER_RESPONSE) as DriverResponse
        bindLocationService()
        MapNavigation(activity).navigateTo(garage)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_navigating, container, false)
    }

    private fun bindLocationService() {
        val bindIntent = Intent(context, Location::class.java)
        val connection = object : ServiceConnection {

            override fun onServiceDisconnected(name: ComponentName?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val localBinder = service as Location.LocalBinder
                location = localBinder.service
                location.setProximityAlert(garage, activity)
            }
        }

        activity.bindService(bindIntent, connection, Context.BIND_AUTO_CREATE)
    }
}
