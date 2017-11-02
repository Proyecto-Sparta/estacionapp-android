package com.sparta.estacionapp.models

import com.google.android.gms.maps.model.LatLng
import com.sparta.estacionapp.models.drawables.ParkingSpace
import java.io.Serializable

class Garage(
        val name: String,
        val id: Int?,
        val email: String?,
        val location: List<Double>?,
        val distance: Double?,
        val pricing: Garage.Pricing?,
        val amenities : List<String>?,
        val outline: List<GaragePoint>?,
        val layouts: List<GarageLayout>?) : Serializable {

    data class Pricing (val bike: Double?, val car: Double?, val pickup: Double?, val id: String?) : Serializable

    data class GaragePoint (val x: Double, val y: Double) : Serializable
    data class GarageLayout (val id: Int, val floor_level: Int, val parking_spaces: List<ParkingSpace>?) : Serializable

    data class SearchResponse (val garages: List<Garage>)

    fun latLng(): LatLng = LatLng(location!![1], location[0])
    fun hasAmenity(amenity: String): Boolean {
        return (amenities is List<String>) && amenities.contains(amenity)
    }

    companion object {
        fun stub() = Garage(
                name = "Estacionamiento 24h",
                id = 1,
                email = "info@estacionapp.com",
                location = listOf(-54.0, -34.0),
                distance = 10.5,
                pricing = Pricing(5.0, 50.0, 80.0, "1"),
                amenities = listOf("bici", "auto", "camioneta", "llaves", "lavado", "inflador", "hours_24", "techado", "manejan"),
                outline = listOf(
                        GaragePoint(100.0, 100.0),
                        GaragePoint(1000.0, 100.0),
                        GaragePoint(1000.0, 1000.0),
                        GaragePoint(100.0, 1000.0),
                        GaragePoint(100.0, 100.0)
                ),
                layouts = listOf(
                        GarageLayout(1, 1, listOf(
                                ParkingSpace(0f, 0f, 100f, 100f),
                                ParkingSpace(500f, 200f, 100f, 100f),
                                ParkingSpace(600f, 600f, 200f, 100f))
                        ),
                        GarageLayout(2, 2, listOf(
                                ParkingSpace(0f, 0f, 120f, 120f),
                                ParkingSpace(550f, 300f, 50f, 100f))
                        )
                )
        )
    }

}

