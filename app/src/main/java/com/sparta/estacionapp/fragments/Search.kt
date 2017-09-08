package com.sparta.estacionapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.ui.adapters.RecyclerViewGarageAdapter


/**
 * A simple [Fragment] subclass.
 */
class Search : Fragment() {

    private lateinit var garageSearchResultsView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //val loginDigest = "Basic Q2hyaXMgTWNDb3JkOnBhc3N3b3Jk"
        //DriverService(context).login(loginDigest)
        val fragment = inflater!!.inflate(R.layout.fragment_search, container, false)

        garageSearchResultsView = fragment.findViewById(R.id.garage_search_results)
        garageSearchResultsView.layoutManager = LinearLayoutManager(activity.applicationContext)

        val garages = mutableListOf(Garage("El conito srl"), Garage("La camioneta loca"))
        garageSearchResultsView.adapter = RecyclerViewGarageAdapter(garages)

        return fragment
    }


    override fun onStart() {
        super.onStart()
    }
}
