package com.sparta.estacionapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.sparta.estacionapp.R
import com.sparta.estacionapp.models.Canvas
import com.sparta.estacionapp.models.Garage

class InnerMap : Fragment() {

    private lateinit var canvas: Canvas
    private lateinit var prev: Button
    private lateinit var next: Button

    private lateinit var garage: Garage
    private lateinit var levels: List<Garage.GarageLayout>

    private var currentLevel: Int = 0

    private lateinit var garage_name: TextView
    private lateinit var garage_email: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val fragment = inflater!!.inflate(R.layout.fragment_inner_map, container, false)
        canvas = fragment.findViewById(R.id.canvas)
        prev = fragment.findViewById(R.id.prev)
        next = fragment.findViewById(R.id.next)

        garage_name = fragment.findViewById(R.id.garage_name)
        garage_email = fragment.findViewById(R.id.garage_email)

        garage = arguments.getSerializable("CURRENT_GARAGE") as Garage

        garage_name.text = garage.name
        garage_email.text = garage.email

        levels = garage.layouts!!.sortedBy { l -> l.floor_level }

        drawLevel(currentLevel)

        setActions()
        return fragment
    }

    private fun setActions() {
        prev.setOnClickListener { prevLayout() }
        next.setOnClickListener { nextLayout() }
    }

    private fun nextLayout() {
        drawLevel(Math.min((currentLevel + 1), levels.size - 1))
    }

    private fun prevLayout() {
        drawLevel(Math.max((currentLevel - 1), 0))
    }

    private fun drawLevel(level : Int) {
        currentLevel = level
        canvas.changeElements(levels[level].parking_spaces!!, garage.outline!!)
    }

}
