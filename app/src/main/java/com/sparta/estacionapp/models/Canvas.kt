package com.sparta.estacionapp.models

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sparta.estacionapp.models.drawables.Drawable


class Canvas : View {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    var elements: List<Drawable> = listOf()
    var outline: List<Garage.GaragePoint> = listOf()
    var parkingSpace: String = ""

    private var outlineWidth: Float = 0f
    private var outlineHeight: Float = 0f

    private var minx: Float = 1f
    private var maxx: Float = 1f
    private var miny: Float = 1f
    private var maxy: Float = 1f

    fun changeElements(newElements: List<Drawable>, currentOutline: List<Garage.GaragePoint>, parkingSpaceId: String) {
        elements = newElements
        outline = currentOutline + listOf(currentOutline[0])
        parkingSpace = parkingSpaceId

        minx = outline.minBy { it.x }!!.x.toFloat()
        maxx = outline.maxBy { it.x }!!.x.toFloat()
        miny = outline.minBy { it.x }!!.y.toFloat()
        maxy = outline.maxBy { it.x }!!.y.toFloat()

        outlineWidth = (maxx - minx)
        outlineHeight = (maxy - miny)

        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        val scale = 1.20f

        canvas.scale(scale, scale, 0f, 0f)
        paintBackground(canvas)
        elements.forEach { canvas.draw(it, parkingSpace) }
        paintOutline(canvas)
    }

    private fun paintOutline(canvas: Canvas) {
        var gp = outline[0]
        val gps = outline.subList(1, outline.size)
        gps.forEach {
            canvas.drawLine(gp.x.toFloat(), gp.y.toFloat(), it.x.toFloat(), it.y.toFloat(), paint())
            gp = it
        }
    }

    fun paint(): Paint {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        return paint
    }

    fun paintBackground(canvas: Canvas) {
        val background = Paint()
        background.style = Paint.Style.FILL
        background.color = Color.GRAY
        canvas.drawRect(0f, 0f, canvas.height.toFloat(), canvas.width.toFloat(), background)
    }
}


fun Canvas.draw(element: Drawable, parkingSpaceId: String) {
    element.drawIn(this, parkingSpaceId)
}

