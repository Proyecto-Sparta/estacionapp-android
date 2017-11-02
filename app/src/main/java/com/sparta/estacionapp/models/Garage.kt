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
                                ParkingSpace(100f, 100f, 100f, 75f),
                                ParkingSpace(100f, 175f, 100f, 75f),
                                ParkingSpace(100f, 250f, 100f, 75f),
                                ParkingSpace(100f, 325f, 100f, 75f),
                                ParkingSpace(100f, 400f, 100f, 75f),
                                ParkingSpace(100f, 475f, 100f, 75f),
                                ParkingSpace(100f, 550f, 100f, 75f),
                                ParkingSpace(100f, 625f, 100f, 75f),
                                ParkingSpace(100f, 700f, 100f, 75f),
                                ParkingSpace(100f, 775f, 100f, 75f),
                                ParkingSpace(100f, 850f, 100f, 75f),
                                ParkingSpace(100f, 925f, 100f, 75f),

                                ParkingSpace(450f, 100f, 100f, 75f),
                                ParkingSpace(450f, 175f, 100f, 75f),
                                ParkingSpace(450f, 250f, 100f, 75f),
                                ParkingSpace(450f, 325f, 100f, 75f),
                                ParkingSpace(450f, 400f, 100f, 75f),
                                ParkingSpace(450f, 475f, 100f, 75f),
                                ParkingSpace(450f, 550f, 100f, 75f),
                                ParkingSpace(450f, 625f, 100f, 75f),
                                ParkingSpace(450f, 700f, 100f, 75f),
                                ParkingSpace(450f, 775f, 100f, 75f),
                                ParkingSpace(450f, 850f, 100f, 75f),
                                ParkingSpace(450f, 925f, 100f, 75f),

                                ParkingSpace(550f, 100f, 100f, 75f),
                                ParkingSpace(550f, 175f, 100f, 75f),
                                ParkingSpace(550f, 250f, 100f, 75f),
                                ParkingSpace(550f, 325f, 100f, 75f),
                                ParkingSpace(550f, 400f, 100f, 75f),
                                ParkingSpace(550f, 475f, 100f, 75f),
                                ParkingSpace(550f, 550f, 100f, 75f),
                                ParkingSpace(550f, 625f, 100f, 75f),
                                ParkingSpace(550f, 700f, 100f, 75f),
                                ParkingSpace(550f, 775f, 100f, 75f),
                                ParkingSpace(550f, 850f, 100f, 75f),
                                ParkingSpace(550f, 925f, 100f, 75f),

                                ParkingSpace(900f, 100f, 100f, 75f),
                                ParkingSpace(900f, 175f, 100f, 75f),
                                ParkingSpace(900f, 250f, 100f, 75f),
                                ParkingSpace(900f, 325f, 100f, 75f),
                                ParkingSpace(900f, 400f, 100f, 75f),
                                ParkingSpace(900f, 475f, 100f, 75f),
                                ParkingSpace(900f, 550f, 100f, 75f),
                                ParkingSpace(900f, 625f, 100f, 75f),
                                ParkingSpace(900f, 700f, 100f, 75f),
                                ParkingSpace(900f, 775f, 100f, 75f),
                                ParkingSpace(900f, 850f, 100f, 75f),
                                ParkingSpace(900f, 925f, 100f, 75f)
                        )),
                        GarageLayout(2, 2, listOf(
                                ParkingSpace(100f, 100f, 100f, 75f),
                                ParkingSpace(100f, 175f, 100f, 75f),
                                ParkingSpace(100f, 250f, 100f, 75f),
                                ParkingSpace(100f, 325f, 100f, 75f),
                                ParkingSpace(100f, 400f, 100f, 75f),
                                ParkingSpace(100f, 475f, 100f, 75f),
                                ParkingSpace(100f, 550f, 100f, 75f),
                                ParkingSpace(100f, 625f, 100f, 75f),
                                ParkingSpace(100f, 700f, 100f, 75f),
                                ParkingSpace(100f, 775f, 100f, 75f),
                                ParkingSpace(100f, 850f, 100f, 75f),
                                ParkingSpace(100f, 925f, 100f, 75f),

                                ParkingSpace(900f, 100f, 100f, 75f),
                                ParkingSpace(900f, 175f, 100f, 75f),
                                ParkingSpace(900f, 250f, 100f, 75f),
                                ParkingSpace(900f, 325f, 100f, 75f),
                                ParkingSpace(900f, 400f, 100f, 75f),
                                ParkingSpace(900f, 475f, 100f, 75f),
                                ParkingSpace(900f, 550f, 100f, 75f),
                                ParkingSpace(900f, 625f, 100f, 75f),
                                ParkingSpace(900f, 700f, 100f, 75f),
                                ParkingSpace(900f, 775f, 100f, 75f),
                                ParkingSpace(900f, 850f, 100f, 75f),
                                ParkingSpace(900f, 925f, 100f, 75f)
                        )),
                        GarageLayout(3, 3, listOf(
                                ParkingSpace(450f, 100f, 100f, 75f),
                                ParkingSpace(450f, 175f, 100f, 75f),
                                ParkingSpace(450f, 250f, 100f, 75f),
                                ParkingSpace(450f, 325f, 100f, 75f),
                                ParkingSpace(450f, 400f, 100f, 75f),
                                ParkingSpace(450f, 475f, 100f, 75f),
                                ParkingSpace(450f, 550f, 100f, 75f),
                                ParkingSpace(450f, 625f, 100f, 75f),
                                ParkingSpace(450f, 700f, 100f, 75f),
                                ParkingSpace(450f, 775f, 100f, 75f),
                                ParkingSpace(450f, 850f, 100f, 75f),
                                ParkingSpace(450f, 925f, 100f, 75f),

                                ParkingSpace(550f, 100f, 100f, 75f),
                                ParkingSpace(550f, 175f, 100f, 75f),
                                ParkingSpace(550f, 250f, 100f, 75f),
                                ParkingSpace(550f, 325f, 100f, 75f),
                                ParkingSpace(550f, 400f, 100f, 75f),
                                ParkingSpace(550f, 475f, 100f, 75f),
                                ParkingSpace(550f, 550f, 100f, 75f),
                                ParkingSpace(550f, 625f, 100f, 75f),
                                ParkingSpace(550f, 700f, 100f, 75f),
                                ParkingSpace(550f, 775f, 100f, 75f),
                                ParkingSpace(550f, 850f, 100f, 75f),
                                ParkingSpace(550f, 925f, 100f, 75f)
                        ))
                )
        )
    }

}

