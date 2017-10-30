package com.sparta.estacionapp.rest

import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Driver
import com.sparta.estacionapp.models.Garage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.Serializable

class DriverService(val context: Context) {

    val api: DriverService = Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(context.getString(R.string.serverAddress))
            .build()
            .create(DriverService::class.java)

    fun login(digest: String, onSuccess: (Driver, String) -> Unit, onError: (Throwable) -> Unit) {
        api.login(digest).enqueue({ driver, response ->
            val jsonWebToken = response.headers().get(context.getString(R.string.authorizationRequestHeader))!!
            jwt = jsonWebToken
            onSuccess.invoke(driver, jwt)
        }, { error -> onError.invoke(error) })

    }

    fun searchGarage(lat: Double, long: Double, maxDistance : Int, onSuccess: (List<Garage>) -> Unit) {
        api.searchGarage(jwt, lat, long, maxDistance).enqueue({ searchResponse, _ ->
            onSuccess.invoke(searchResponse.garages)
        })
    }

    fun save(driver: Driver, onSuccess: (Driver) -> Unit) {
        api.save(jwt, driver.id!!, DriverPatch.from(driver)).enqueue({ driverId, _ ->
            onSuccess.invoke(driverId)
        })
    }

    interface DriverService {

        @GET("/api/drivers/login")
        fun login(@Header("Authorization") loginDigest: String): Call<Driver>

        @GET("/api/garages/search")
        fun searchGarage(@Header("Authorization") loginDigest: String,
                         @Query("latitude") lat: Double,
                         @Query("longitude") long: Double,
                         @Query("max_distance") maxDistance : Int): Call<Garage.SearchResponse>

        @PATCH("/api/drivers/{id}")
        fun save(@Header("Authorization") loginDigest: String,
                 @Path("id") id: Int,
                 @Body driver: DriverPatch): Call<Driver>

    }

    class DriverPatch(
            var full_name: String?,
            var email: String?,
            var id: Int?,
            var vehicle : com.sparta.estacionapp.models.Driver.Vehicle?) : Serializable {

        companion object {
            fun from(driver : Driver) : DriverPatch {
                return DriverPatch(driver.name, driver.email, driver.id, driver.vehicle)
            }
        }

    }

    companion object {

        lateinit var jwt : String

        private val firebase : FirebaseDatabase = FirebaseDatabase.getInstance()

        fun reserveGarage(garage: Garage, driver: Driver) {
            firebase.getReference("garages")
                    .child(garage.id.toString())
                    .child(driver.id.toString())
                    .setValue(driver)
        }

    }
}