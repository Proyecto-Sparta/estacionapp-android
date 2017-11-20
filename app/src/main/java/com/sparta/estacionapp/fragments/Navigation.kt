package com.sparta.estacionapp.fragments


import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.MapFragment
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.models.MapNavigation
import com.sparta.estacionapp.services.Constants
import com.sparta.estacionapp.services.Location


class Navigation : Fragment() {

    private lateinit var location: Location
    private lateinit var garage: Garage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        garage = arguments.getSerializable(Constants.CURRENT_GARAGE) as Garage
        initBroadcastReceiver()
        bindLocationService()
        MapNavigation(activity).navigateTo(garage);
    }

    private fun initBroadcastReceiver() {
        val broadcastReceiver = GarageProximity()
        val filter = IntentFilter(Location.PROXIMITY_ACTION)
        activity.registerReceiver(broadcastReceiver, filter)
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


    class GarageProximity : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            println("HOLA" + intent.action)
        }
    }
}
