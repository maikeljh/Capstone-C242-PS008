package com.example.culinairy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.culinairy.databinding.ActivityRegisterBinding
import com.example.culinairy.ui.register.RegisterViewModel
import androidx.lifecycle.Observer

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView5.setOnClickListener {
            navigateToLogin()
        }

        // register
        binding.registerButton.setOnClickListener {
            val name = binding.namaEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()
            val address = binding.posisiEt.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && address.isNotEmpty()) {
                if (isValidEmail(email) && isValidPassword(password)) {
                    toggleButtonState(false)
                    toggleLoading(true)
                    viewModel.register(name, email, password, address)
                } else {
                    Toast.makeText(this, "Please insert a valid email and a minimum 6 chars password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter name, email, password, and address", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.registrationStatus.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
            toggleButtonState(true)
            toggleLoading(false)
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            toggleButtonState(true)
            toggleLoading(false)
        })
    }

    private fun toggleButtonState(enabled: Boolean) {
        binding.registerButton.isEnabled = enabled
        binding.textView5.isEnabled = enabled
    }

    private fun toggleLoading(show: Boolean) {
        binding.loadingAnimation.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        binding.darkOverlay.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^.{6,}$".toRegex()
        return passwordRegex.matches(password)
    }
}
