package com.sparta.estacionapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage

class RecyclerViewGarageAdapter(var garages: List<Garage>) : RecyclerView.Adapter<Garage.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Garage.ViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.garage_search_card, parent, false)
        return Garage.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Garage.ViewHolder?, position: Int) {
        holder!!.bindWith(garages[position])
    }

    override fun getItemCount(): Int = garages.size
}