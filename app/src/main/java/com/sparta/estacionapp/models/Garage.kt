package com.sparta.estacionapp.models

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.sparta.estacionapp.R

class Garage(val name: String) {


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private var garageName: TextView = itemView!!.findViewById(R.id.garage_name)

        fun bindWith(garage: Garage) {
            garageName.text = garage.name
        }
    }
}
