package com.sparta.estacionapp.models

import android.content.SharedPreferences
import java.io.Serializable

class Driver(
        val name: String?,
        val email: String?,
        val id: Int?,
        val vehicle : Vehicle?) : Serializable {

    data class Vehicle(val plate : String, val type: String)

    companion object {
        private lateinit var current : Driver

        fun current() : Driver {
            return current
        }

        fun logout(preferences: SharedPreferences) {
            preferences
                    .edit()
                    .remove("DriverId")
                    .remove("DriverName")
                    .remove("DriverEmail")
                    .remove("DriverVehicleType")
                    .remove("DriverVehiclePlate")
                    .apply()
        }

        fun login(driver : Driver, preferences: SharedPreferences) {
            preferences
                    .edit()
                    .putInt("DriverId", driver.id!!)
                    .putString("DriverName", driver.name)
                    .putString("DriverEmail", driver.email)
                    .putString("DriverVehicleType", driver.vehicle!!.type)
                    .putString("DriverVehiclePlate", driver.vehicle.plate)
                    .apply()
            current = driver
        }

        fun setCurrentFrom(preferences: SharedPreferences) {
            val id = preferences.getInt("DriverId", 0)
            val name = preferences.getString("DriverName", "Juan Perez")
            val email = preferences.getString("DriverEmail", "juanperez@gmail.com")
            val type = preferences.getString("DriverVehicleType", "car")
            val plate = preferences.getString("DriverVehiclePlate", "ABC 123")
            current = Driver(name, email, id, Vehicle(plate, type))
        }
    }

}