package com.sparta.estacionapp.models.drawables


import android.graphics.Color
import android.graphics.Paint
import android.graphics.Canvas as AndroidCanvas

class ParkingSpace(val x: Float, val y: Float, val height: Float, val width: Float) : Drawable {

    override fun drawIn(canvas: AndroidCanvas) {
        canvas.drawRect(x + offset(), y + offset(), height + x, width + y, paint())
    }

    private fun offset(): Float = lineWidth()

    private fun lineWidth(): Float = 5f

    fun paint(): Paint {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = lineWidth()
        paint.style = Paint.Style.STROKE
        return paint
    }
}