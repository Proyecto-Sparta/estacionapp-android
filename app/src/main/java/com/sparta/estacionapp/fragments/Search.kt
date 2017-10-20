package com.sparta.estacionapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sparta.estacionapp.R
import kotterknife.bindView


/**
 * A simple [Fragment] subclass.
 */
class Search : Fragment() {

    private lateinit var garageSearchResultsView: RecyclerView

    private val search: Button by bindView(R.id.ws_button)


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val fragment = inflater!!.inflate(R.layout.fragment_search, container, false)

        garageSearchResultsView = fragment.findViewById(R.id.garage_search_results)
        garageSearchResultsView.layoutManager = LinearLayoutManager(activity.applicationContext)

//        val garages = mutableListOf(Garage("El conito srl"), Garage("La camioneta loca"))
//        garageSearchResultsView.adapter = RecyclerViewGarageAdapter(garages)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
               var a = p0!!.getValue(String::class.java)
            }

        })

        myRef.setValue("Hello, World!")

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


    override fun onStart() {
        super.onStart()
    }
}
