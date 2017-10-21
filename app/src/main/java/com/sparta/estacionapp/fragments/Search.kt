package com.sparta.estacionapp.fragments


import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.rest.DriverService
import com.sparta.estacionapp.ui.adapters.RecyclerViewGarageAdapter


/**
 * A simple [Fragment] subclass.
 */
class Search : Fragment() {

    private lateinit var garageSearchResultsView: RecyclerView

    private lateinit var search: Button
    private lateinit var seekRadio: SeekBar
    private lateinit var seekBarValue : TextView
    private lateinit var placeAutocompleteFragment: SupportPlaceAutocompleteFragment

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val fragment = inflater!!.inflate(R.layout.fragment_search, container, false)

        seekRadio = fragment.findViewById(R.id.seek_radio)
        seekBarValue = fragment.findViewById(R.id.txt_radio)

        garageSearchResultsView = fragment.findViewById(R.id.garage_search_results)
        garageSearchResultsView.layoutManager = LinearLayoutManager(activity.applicationContext)

        seekRadio.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar : SeekBar, progress : Int, fromUser : Boolean) {
                seekBarValue.text = "$progress meters"
            }

            override fun onStartTrackingTouch(seekBar : SeekBar) {}

            override fun onStopTrackingTouch(seekBar : SeekBar) {}
        })

        initPlaceAutocompleteFragment()

        seekRadio.progress = 1000

        // val garages = mutableListOf(Garage("El conito srl"), Garage("La camioneta loca"))

//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message")
//        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onCancelled(p0: DatabaseError?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot?) {
//               var a = p0!!.getValue(String::class.java)
//            }
//
//        })
//
//        myRef.setValue("Hello, World!")

        //search.setOnClickListener { _ -> requestGarage() }
        return fragment
    }

    private fun requestGarage() {
//        val socket = Socket("ws://localhost:4000/socket")
//        socket.connect()
//
//        val channel = socket.chan("garage:sarasa", null)
//        channel.join()
    }

    fun initPlaceAutocompleteFragment() {
        placeAutocompleteFragment = childFragmentManager.findFragmentById(R.id.search_fragment) as SupportPlaceAutocompleteFragment
        placeAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                DriverService(activity).searchGarage(place.latLng.latitude, place.latLng.longitude, seekRadio.progress, {garages ->
                    garageSearchResultsView.adapter = RecyclerViewGarageAdapter(garages)
                })
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status)
            }
        })
    }

}
