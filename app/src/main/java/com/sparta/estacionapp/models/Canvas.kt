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

    fun changeElements(newElements: List<Drawable>, currentOutline: List<Garage.GaragePoint>) {
        elements = newElements
        outline = currentOutline
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!
        canvas.scale(1f, -1f, canvas.width.toFloat() / 2, canvas.height.toFloat() / 2)
        paintBackground(canvas)
        paintOutline(canvas)
        elements.forEach { canvas.draw(it) }
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


fun Canvas.draw(element: Drawable) {
    element.drawIn(this)
}

