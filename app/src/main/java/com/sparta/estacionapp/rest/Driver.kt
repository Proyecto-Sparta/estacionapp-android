package com.sparta.estacionapp.rest

import android.content.Context
import android.util.Log
import com.sparta.estacionapp.R
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.util.function.Consumer


class DriverService(val context: Context) {

    val api: DriverService = Retrofit
            .Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(context.getString(R.string.serverAddress))
            .build()
            .create(DriverService::class.java)

    fun login(digest: String, onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        api.login(digest).enqueue({ _, response ->
            onSuccess.invoke(response.headers().get(context.getString(R.string.authorizationRequestHeader))!!)
        }, { error -> onError.invoke(error) })

    }

    class LoginResponse(val status: String, var jwt: String) {
        override fun toString(): String {
            return "LoginResponse(status='$status', jwt='$jwt')"
        }
    }

    interface DriverService {

        @GET("/api/drivers/login")
        fun login(@Header("Authorization") loginDigest: String): Call<LoginResponse>
    }
}

