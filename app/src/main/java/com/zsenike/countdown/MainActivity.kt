package com.zsenike.countdown

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.zsenike.countdown.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateCountdown()
            handler.postDelayed(this, 1000)
        }
    }

    // Ziel: 19. November 2026, 16:00 Uhr
    private val targetTime: Long by lazy {
        Calendar.getInstance().apply {
            set(2026, Calendar.NOVEMBER, 19, 16, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateCountdown()
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

    private fun updateCountdown() {
        val now = System.currentTimeMillis()
        val diff = targetTime - now

        if (diff <= 0) {
            binding.days.text = "00"
            binding.hours.text = "00"
            binding.minutes.text = "00"
            binding.seconds.text = "00"
            binding.message.text = "🎉 Es ist soweit!"
            return
        }

        val days = TimeUnit.MILLISECONDS.toDays(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60

        binding.days.text = "%02d".format(days)
        binding.hours.text = "%02d".format(hours)
        binding.minutes.text = "%02d".format(minutes)
        binding.seconds.text = "%02d".format(seconds)

        binding.message.text = when {
            days > 30 -> "Noch etwas Zeit …"
            days > 7 -> "Es rückt näher!"
            days > 1 -> "Bald ist es soweit 🔥"
            days == 1L -> "Nur noch ein Tag!"
            else -> "Heute ist der große Tag!"
        }
    }
}
