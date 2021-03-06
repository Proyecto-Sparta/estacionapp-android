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
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.sparta.estacionapp.R
import com.sparta.estacionapp.activities.Home
import com.sparta.estacionapp.models.Canvas
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.models.MapNavigation
import com.sparta.estacionapp.models.responses.DriverResponse
import com.sparta.estacionapp.services.Constants
import com.sparta.estacionapp.services.Location

class InnerMap : Fragment() {

    private lateinit var location: Location

    private lateinit var canvas: Canvas
    private lateinit var prev: Button
    private lateinit var next: Button
    private lateinit var llegue: ImageButton

    private lateinit var garage: Garage
    private lateinit var driverResponse: DriverResponse

    private lateinit var levels: List<Garage.GarageLayout>

    private var currentLevel: Int = 0

    private lateinit var garage_name: TextView
    private lateinit var garage_email: TextView
    private lateinit var txt_floor: TextView
    private lateinit var outline: List<Garage.GaragePoint>

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragment = inflater!!.inflate(R.layout.fragment_inner_map, container, false)
        canvas = fragment.findViewById(R.id.canvas)
        prev = fragment.findViewById(R.id.prev)
        next = fragment.findViewById(R.id.next)
        llegue = fragment.findViewById(R.id.llegue)
        txt_floor = fragment.findViewById(R.id.txt_floor)

        garage = arguments.getSerializable(Constants.CURRENT_GARAGE) as Garage
        driverResponse = arguments.getSerializable(Constants.DRIVER_RESPONSE) as DriverResponse

        val preferences = context.getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE)
        val edit = preferences.edit()
        edit.putString(Constants.CURRENT_GARAGE, Gson().toJson(garage, Garage::class.java))
        edit.putString(Constants.DRIVER_RESPONSE, Gson().toJson(driverResponse, DriverResponse::class.java))
        edit.apply()

        garage_name = fragment.findViewById(R.id.garage_name)
        garage_email = fragment.findViewById(R.id.garage_email)

        garage_name.text = garage.name
        garage_email.text = garage.email

        levels = if (garage.layouts == null) Garage.stub().layouts!! else garage.layouts!!
        outline = if (garage.outline == null) Garage.stub().outline!! else garage.outline!!

        currentLevel = if (driverResponse.floor == null) 0 else driverResponse.floor!!.toInt() - 1
        drawLevel(currentLevel)

        setActions()

        bindLocationService()
        MapNavigation(activity).navigateTo(garage)

        return fragment
    }

    private fun setActions() {
        prev.setOnClickListener { prevLayout() }
        next.setOnClickListener { nextLayout() }
        llegue.setOnClickListener {
            val preferences = context.getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE)
            val edit = preferences.edit()
            edit.remove(Constants.CURRENT_GARAGE)
            edit.remove(Constants.DRIVER_RESPONSE)
            edit.apply()
            Toast.makeText(context, "Gracias por usar EstacionApp", Toast.LENGTH_LONG).show()
            (activity as Home).loadSearchFragment()
        }
    }

    private fun nextLayout() {
        val level = Math.min((currentLevel + 1), levels.size - 1)
        drawLevel(level)
        changeFloorText(level)
    }

    private fun prevLayout() {
        val level = Math.max((currentLevel - 1), 0)
        drawLevel(level)
        changeFloorText(level)
    }

    private fun changeFloorText(level: Int) {
        txt_floor.text = "Piso ${level + 1} de ${levels.size}"
    }

    private fun bindLocationService() {
        val bindIntent = Intent(activity, Location::class.java)
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

    private fun drawLevel(level: Int) {
        currentLevel = level
        canvas.changeElements(levels[level].parking_spaces!!, outline, driverResponse.parkingSpace!!)
    }


}