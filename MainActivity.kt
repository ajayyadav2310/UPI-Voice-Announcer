package com.ajay.upivoice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (20 * resources.displayMetrics.density).toInt()
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(0xFFF4F7FB.toInt())
        }
        val title = TextView(this).apply {
            text = "UPI Voice Announcer"
            textSize = 26f
            setTextColor(0xFF0F172A.toInt())
            gravity = Gravity.CENTER
        }
        val desc = TextView(this).apply {
            text = "\nPayment notification aate hi Hindi voice announcement.\n\nExample: “100 rupaye Navi par prapt hue.”\n\nStart karne ke liye Notification Access enable karein.\n"
            textSize = 17f
            setTextColor(0xFF334155.toInt())
            gravity = Gravity.CENTER
        }
        val button = Button(this).apply {
            text = "Enable Notification Access"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        val note = TextView(this).apply {
            text = "\nPrivacy: App sirf notification ka text locally process karta hai. Yeh bank/UPI server se payment verify nahi karta. Phone par aayi notification fake ya galat bhi ho sakti hai."
            textSize = 13f
            setTextColor(0xFF64748B.toInt())
            gravity = Gravity.CENTER
        }
        layout.addView(title)
        layout.addView(desc)
        layout.addView(button)
        layout.addView(note)
        setContentView(layout)
    }
}
