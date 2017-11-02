package com.sparta.estacionapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Canvas
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.models.drawables.ParkingSpace

class InnerMap : Fragment() {

    private lateinit var canvas: Canvas
    private lateinit var prev: Button
    private lateinit var next: Button

    private lateinit var garage: Garage

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragment = inflater!!.inflate(R.layout.fragment_inner_map, container, false)
        canvas = fragment.findViewById(R.id.canvas)
        prev = fragment.findViewById(R.id.prev)
        next = fragment.findViewById(R.id.next)

        garage = arguments.getSerializable("CURRENT_GARAGE") as Garage

        setActions()
        return fragment
    }

    private fun setActions() {
        prev.setOnClickListener { _ -> canvas.changeElements(listOf(ParkingSpace(0f, 0f, 100f, 100f), ParkingSpace(500f, 200f, 100f, 100f), ParkingSpace(600f, 600f, 200f, 100f))) }
        next.setOnClickListener { _ -> canvas.changeElements(listOf(ParkingSpace(100f, 100f, 300f, 300f))) }
    }

}
