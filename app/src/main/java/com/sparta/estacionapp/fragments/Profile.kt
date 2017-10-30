package com.sparta.estacionapp.fragments


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Driver
import com.sparta.estacionapp.rest.DriverService

class Profile : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragment = inflater!!.inflate(R.layout.fragment_profile, container, false)

        val spinner = fragment.findViewById<Spinner>(R.id.vehicle_sp)

        val adapter = ArrayAdapter.createFromResource(context, R.array.vehicles_types, android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        fragment.findViewById<Button>(R.id.save_driver).setOnClickListener { saveDriver(fragment) }

        return fragment
    }

    private fun saveDriver(fragment : View) {
        val driver = Driver.current()
        driver.name = fragment.findViewById<EditText>(R.id.name_txt).text.toString()
        driver.email = fragment.findViewById<EditText>(R.id.email_txt).text.toString()
        driver.vehicle = Driver.Vehicle(
                fragment.findViewById<EditText>(R.id.plate_txt).text.toString(),
                listOf("bike", "car", "pickup")[fragment.findViewById<Spinner>(R.id.vehicle_sp).selectedItemPosition]
        )
        DriverService(context).save(driver, { _ ->
            Driver.login(driver, context.getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE))
        })
    }

}
