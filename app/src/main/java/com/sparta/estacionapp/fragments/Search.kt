package com.sparta.estacionapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sparta.estacionapp.R
import com.sparta.estacionapp.rest.Driver


/**
 * A simple [Fragment] subclass.
 */
class Search : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val loginDigest = "Basic Q2hyaXMgTWNDb3JkOnBhc3N3b3Jk"
        Driver(context).login(loginDigest)
        return inflater!!.inflate(R.layout.fragment_search, container, false)
    }

}// Required empty public constructor
