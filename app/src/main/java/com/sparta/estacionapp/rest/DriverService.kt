package com.sparta.estacionapp.rest

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Driver
import com.sparta.estacionapp.models.Garage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

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

    fun searchGarage(lat: Double, long: Double, maxDistance: Int, onSuccess: (List<Garage>) -> Unit) {
        api.searchGarage(jwt, lat, long, maxDistance).enqueue({ searchResponse, _ ->
            onSuccess.invoke(searchResponse.garages)
        })
    }

    fun save(driver: Driver, onSuccess: () -> Unit) {
        api.save(jwt, driver).enqueue({ _, _ ->
            onSuccess.invoke()
        })
    }

    interface DriverService {

        @POST("/api/drivers/login")
        fun login(@Header("Authorization") loginDigest: String): Call<Driver>

        @GET("/api/garages/search")
        fun searchGarage(@Header("Authorization") loginDigest: String,
                         @Query("latitude") lat: Double,
                         @Query("longitude") long: Double,
                         @Query("max_distance") maxDistance : Int): Call<Garage.SearchResponse>

        @PATCH("/api/drivers")
        fun save(@Header("Authorization") loginDigest: String,
                 @Body driver: Driver): Call<Unit>

    }

    companion object {

        lateinit var jwt: String

        private val firebase: FirebaseDatabase = FirebaseDatabase.getInstance()

        private val garages: DatabaseReference = firebase.getReference("garages")
        private val drivers: DatabaseReference = firebase.getReference("drivers")

        fun reserveGarage(garage: Garage, driver: Driver) {
            garages
                    .child(garage.id.toString())
                    .child(driver.id.toString())
                    .setValue(driver)
        }

        fun reservationResponse(driver: Driver, onResponse: DriverReservationResponse) {
            val ref = drivers.child(driver.id.toString())
            ref.childAdded(onResponse)
        }
    }
}
