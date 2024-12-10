package com.example.culinairy
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import com.example.culinairy.databinding.ActivityLoginBinding
import com.example.culinairy.ui.login.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                setButtonsEnabled(false)
                viewModel.login(this@LoginActivity, email, password) { state ->
                    handleLoginState(state)
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            navigateToRegister()
        }

        binding.googleButton.setOnClickListener {
            navigateToMain()
        }
    }

    private fun handleLoginState(state: LoginViewModel.LoginState) {
        setButtonsEnabled(true)
        when (state) {
            is LoginViewModel.LoginState.Success -> {
                Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }
            is LoginViewModel.LoginState.Failure -> {
                Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
            }
            is LoginViewModel.LoginState.Error -> {
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is LoginViewModel.LoginState.InvalidCredentials -> {
                Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setButtonsEnabled(isEnabled: Boolean) {
        binding.loginButton.isEnabled = isEnabled
        binding.registerButton.isEnabled = isEnabled
        binding.googleButton.isEnabled = isEnabled
    }
}
