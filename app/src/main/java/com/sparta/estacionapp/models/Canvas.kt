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


    fun changeElements(newElements: List<Drawable>) {
        elements = newElements
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!
        canvas.scale(1f, -1f, canvas.width.toFloat() / 2, canvas.height.toFloat() / 2)
        paintBackground(canvas)
        elements.forEach { canvas.draw(it) }
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

