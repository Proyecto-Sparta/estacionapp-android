package com.sparta.estacionapp.models

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

class Garage(
        val name: String,
        val id: Int?,
        val email: String?,
        val location: List<Double>?,
        val distance: Double?,
        val pricing: Garage.Pricing?,
        val amenities : List<String>?) : Serializable {

    data class Pricing (val bike: Double?, val car: Double?, val pickup: Double?, val id: String?) : Serializable

    data class SearchResponse (val garages: List<Garage>)

    fun latLng(): LatLng = LatLng(location!![1], location[0])
    fun hasAmenity(amenity: String): Boolean {
        return (amenities is List<String>) && amenities.contains(amenity)
    }

    companion object {
        fun stub() = Garage("Estacion App", 1, "info@estacionapp.com", null, null, null,  null)
    }

}

