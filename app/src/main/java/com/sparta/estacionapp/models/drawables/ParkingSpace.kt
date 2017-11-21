package com.sparta.estacionapp.models.drawables


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import java.io.Serializable
import android.graphics.Canvas as AndroidCanvas

class ParkingSpace(val x: Float,
                   val y: Float,
                   val height: Float,
                   val width: Float,
                   val angle: Int = 0,
                   val occupied: Boolean = false,
                   val shape: String = "square",
                   val id: String = "") : Drawable, Serializable {

    @SuppressLint("NewApi")
    override fun drawIn(canvas: AndroidCanvas, parkingSpaceId: String) {
        canvas.drawRoundRect(x + offset(), y + offset(), width + x, height + y, height/5, height/5, paintFill(parkingSpaceId))
        canvas.drawRoundRect(x + offset(), y + offset(), width + x, height + y, height/5, height/5, paintStroke())
    }

    private fun offset(): Float = lineWidth() / 2

    private fun lineWidth(): Float = 5f

    fun paintFill(parkingSpaceId: String): Paint {
        val paint = Paint()
        paint.color = getColorFor(parkingSpaceId)
        paint.strokeWidth = 0f
        paint.style = Paint.Style.FILL
        return paint
    }

    fun paintStroke(): Paint {
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = lineWidth()
        paint.style = Paint.Style.STROKE
        return paint
    }

    private fun getColorFor(parkingSpaceId: String): Int {
        return if (parkingSpaceId != id) {
            if (occupied) Color.rgb(244, 67, 54) else Color.rgb(76, 175, 80)
        } else {
            Color.rgb(63, 81, 181)
        }
    }
}