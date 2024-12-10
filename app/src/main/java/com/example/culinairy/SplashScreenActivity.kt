package com.example.culinairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.culinairy.databinding.ActivitySplashScreenBinding
import com.example.culinairy.ui.splash.SplashViewModel
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private val splashDisplay: Long = 3000
    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                val isTokenValid = viewModel.isTokenValid(this@SplashScreenActivity)
                if (isTokenValid) {
                    navigateToMain()
                } else {
                    navigateToLogin()
                }
            }
        }, splashDisplay)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}