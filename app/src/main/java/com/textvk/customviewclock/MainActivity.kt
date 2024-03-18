package com.textvk.customviewclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.textvk.customviewclock.clockView.clockView

class MainActivity : AppCompatActivity() {
    private lateinit var clockView: clockView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clockView = findViewById(R.id.clockView)


        clockView.setOnClickListener {
            clockView.layoutParams.width += 50
            clockView.layoutParams.height += 50
            clockView.requestLayout()
        }

    }
}