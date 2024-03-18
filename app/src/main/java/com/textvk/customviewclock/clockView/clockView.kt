package com.textvk.customviewclock.clockView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class clockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val calendar: Calendar = Calendar.getInstance()
    private var hour: Int = 0
    private var minute: Int = 0
    private var second: Int = 0

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.CYAN
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        textAlign = Paint.Align.CENTER
        textSize = 50f
    }

    private val numbers = arrayOf("3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "1", "2")

    private val hand = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: android.os.Message) {
            updateClock()
            sendEmptyMessageDelayed(0, 1000)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val radius = width / 2f

        paint.strokeWidth = 20f
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(radius, radius, radius - paint.strokeWidth / 2f, paint)

        // Цифры
        paint.strokeWidth = 4f
        textPaint.color = Color.GREEN
        textPaint.textSize = 70f
        textPaint.strokeWidth = 2f
        textPaint.style = Paint.Style.FILL_AND_STROKE
        for (i in 0 until 12) {
            val x = radius + (radius - 90f) * cos(Math.toRadians((i * 30).toDouble())).toFloat()
            val y = radius + (radius - 90f) * sin(Math.toRadians((i * 30).toDouble())).toFloat()
            canvas.drawText(numbers[i], x, y + textPaint.textSize / 3f, textPaint)
        }

        // Центральная точка
        paint.style = Paint.Style.FILL
        canvas.drawCircle(radius, radius, 12f, paint)

        // Часовая стрелка
        paint.strokeWidth = 12f
        paint.color = Color.DKGRAY
        paint.strokeCap = Paint.Cap.ROUND
        paint.setShadowLayer(10f, 0f, 0f, Color.BLACK)
        canvas.save()
        canvas.rotate((hour + minute / 60f) * 30f, radius, radius)
        canvas.drawLine(radius, radius + 20f, radius, radius - radius / 2f, paint)
        canvas.restore()

        // Минутная
        paint.strokeWidth = 4.5f
        canvas.save()
        canvas.rotate(minute * 6f, radius, radius)
        canvas.drawLine(radius, radius + 20f, radius, radius - radius * 0.7f, paint)
        canvas.restore()

        // Секундная
        paint.strokeWidth = 2f
        canvas.save()
        canvas.rotate(second * 6f, radius, radius)
        canvas.drawLine(radius, radius + 20f, radius, radius - radius * 0.9f, paint)
        canvas.restore()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        hand.sendEmptyMessage(0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        hand.removeCallbacksAndMessages(null)
    }

    private fun updateClock() {
        calendar.timeInMillis = System.currentTimeMillis()
        hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)
        invalidate()
    }
}
