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

    private val vehiclesTypes = listOf("bike", "car", "pickup")
    private val driver = Driver.current()

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var plate: EditText
    private lateinit var vehicleType: Spinner

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragment = inflater!!.inflate(R.layout.fragment_profile, container, false)

        val spinner = fragment.findViewById<Spinner>(R.id.vehicle_sp)
        val adapter = ArrayAdapter.createFromResource(context, R.array.vehicles_types, android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        name = fragment.findViewById<EditText>(R.id.name_txt)
        email = fragment.findViewById<EditText>(R.id.email_txt)
        plate = fragment.findViewById<EditText>(R.id.plate_txt)
        vehicleType = fragment.findViewById<Spinner>(R.id.vehicle_sp)

        name.setText(driver.name)
        email.setText(driver.email)
        plate.setText(driver.vehicle!!.plate)

        vehicleType.setSelection(vehiclesTypes.indexOf(driver.vehicle!!.type))

        fragment.findViewById<Button>(R.id.save_driver).setOnClickListener { saveDriver() }

        return fragment
    }

    private fun saveDriver() {
        val preferences = context.getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE)

        driver.name = name.text.toString()
        driver.email = email.text.toString()
        driver.vehicle = Driver.Vehicle(plate.text.toString(), vehiclesTypes[vehicleType.selectedItemPosition])

        DriverService(context).save(driver) { _ -> Driver.login(driver, preferences) }
    }

}