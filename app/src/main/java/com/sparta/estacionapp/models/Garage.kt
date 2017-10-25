package com.sparta.estacionapp.models

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.sparta.estacionapp.R
import java.io.Serializable

class Garage(
        val name: String,
        val id: Int?,
        val email: String?,
        val location: List<Double>?,
        val distance: Double?,
        val pricing: Garage.Pricing?) : Serializable {

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private var garageName: TextView = itemView!!.findViewById(R.id.garage_name)

        fun bindWith(garage: Garage) {
            garageName.text = garage.name
        }
    }

    data class Pricing (val bike: Double?, val car: Double?, val pickup: Double?, val id: String?) : Serializable

    data class SearchResponse (val garages: List<Garage>)

    fun latLng(): LatLng = LatLng(location!![1], location[0])

    companion object {
        fun stub() = Garage("Estacion App", 1, "info@estacionapp.com", null, null, null)
    }

}

