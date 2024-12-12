package com.example.culinairy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.culinairy.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private var _binding: ActivityIntroBinding? = null
    private val binding get() = _binding!!
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updatePage()

        binding.skipText.setOnClickListener {
            navigateToLogin()
        }

        binding.nextText.setOnClickListener {
            if (currentPage < 3) {
                currentPage++
                updatePage()
            }
        }

        binding.getStartedButton.setOnClickListener {
            navigateToLogin()
        }
    }
    private fun updatePage() {
        when (currentPage) {
            1 -> {
                binding.introImage1.visibility = android.view.View.VISIBLE
                binding.introTitle1.visibility = android.view.View.VISIBLE
                binding.introDesc1.visibility = android.view.View.VISIBLE
                binding.introImage2.visibility = android.view.View.GONE
                binding.introTitle2.visibility = android.view.View.GONE
                binding.introDesc2.visibility = android.view.View.GONE
                binding.introImage3.visibility = android.view.View.GONE
                binding.introTitle3.visibility = android.view.View.GONE
                binding.introDesc3.visibility = android.view.View.GONE
                binding.getStartedButton.visibility = android.view.View.GONE
                binding.nextText.visibility = android.view.View.VISIBLE
                binding.dot1.setImageResource(R.drawable.dot_active)
                binding.dot2.setImageResource(R.drawable.dot_inactive)
                binding.dot3.setImageResource(R.drawable.dot_inactive)
            }
            2 -> {
                binding.introImage1.visibility = android.view.View.GONE
                binding.introTitle1.visibility = android.view.View.GONE
                binding.introDesc1.visibility = android.view.View.GONE
                binding.introImage2.visibility = android.view.View.VISIBLE
                binding.introTitle2.visibility = android.view.View.VISIBLE
                binding.introDesc2.visibility = android.view.View.VISIBLE
                binding.introImage3.visibility = android.view.View.GONE
                binding.introTitle3.visibility = android.view.View.GONE
                binding.introDesc3.visibility = android.view.View.GONE
                binding.getStartedButton.visibility = android.view.View.GONE
                binding.nextText.visibility = android.view.View.VISIBLE
                binding.dot1.setImageResource(R.drawable.dot_inactive)
                binding.dot2.setImageResource(R.drawable.dot_active)
                binding.dot3.setImageResource(R.drawable.dot_inactive)
            }
            3 -> {
                binding.introImage1.visibility = android.view.View.GONE
                binding.introTitle1.visibility = android.view.View.GONE
                binding.introDesc1.visibility = android.view.View.GONE
                binding.introImage2.visibility = android.view.View.GONE
                binding.introTitle2.visibility = android.view.View.GONE
                binding.introDesc2.visibility = android.view.View.GONE
                binding.introImage3.visibility = android.view.View.VISIBLE
                binding.introTitle3.visibility = android.view.View.VISIBLE
                binding.introDesc3.visibility = android.view.View.VISIBLE
                binding.getStartedButton.visibility = android.view.View.VISIBLE
                binding.nextText.visibility = android.view.View.GONE
                binding.skipText.visibility = android.view.View.GONE
                binding.dot1.setImageResource(R.drawable.dot_inactive)
                binding.dot2.setImageResource(R.drawable.dot_inactive)
                binding.dot3.setImageResource(R.drawable.dot_active)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

