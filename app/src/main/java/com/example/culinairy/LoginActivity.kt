package com.example.culinairy
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.culinairy.databinding.ActivityLoginBinding
import com.example.culinairy.model.LoginRequestBody
import com.example.culinairy.repository.AuthRepository
import com.example.culinairy.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                login(email, password)
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

    private fun login(email: String, password: String) {

        setButtonsEnabled(false)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = AuthRepository().login(LoginRequestBody(email, password))
                if (response.isSuccessful) {
                    Log.d("Sign In Activity - Login", "JWT TOKEN : ${response.body()?.token}")
                    val token = response.body()?.token ?: return@launch
                    val exp = response.body()?.exp ?: return@launch
                    TokenManager.saveExpToken(this@LoginActivity, exp)
                    TokenManager.saveEncryptedToken(this@LoginActivity, token)
                    Toast.makeText(this@LoginActivity, "Login success.", Toast.LENGTH_SHORT).show()
                    Log.d("Sign In Activity - Login", "Login success")
                    navigateToMain()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                    Log.e("Sign In Activity - Login", "Login failed. Please check your credentials.")
                }
            } catch (e: HttpException) {
                Toast.makeText(this@LoginActivity, "Login failed. ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Sign In Activity - Login", "Login failed. Http exception: ", e)
            } catch (e: Throwable) {
                Toast.makeText(this@LoginActivity, "Login failed. ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Sign In Activity - Login", "Login failed. Throwable: ", e)
            } finally {
                setButtonsEnabled(true)
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
