package com.princeoprince.organisednote.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.princeoprince.organisednote.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeFullScreen()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        delayActivity()
    }

    private fun makeFullScreen() {
        // Remote title
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        hideStatusBar()

        // Hide the toolbar
        supportActionBar?.hide()

    }

    private fun hideStatusBar() {
        WindowCompat.getInsetsController(window, window.decorView)
            ?.hide(
                WindowInsetsCompat.Type.statusBars() or
                        WindowInsetsCompat.Type.navigationBars()
            )
    }

    private fun delayActivity() {
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
                    startActivity(Intent(this, ItemsActivity::class.java))
                    // Animate the loading of a new activity
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    // Close the activity
                    finish()
                }
        handler.postDelayed(runnable, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}