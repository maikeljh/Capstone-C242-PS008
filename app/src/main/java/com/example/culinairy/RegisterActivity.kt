package com.example.culinairy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.culinairy.databinding.ActivityRegisterBinding
import com.example.culinairy.model.LoginRequestBody
import com.example.culinairy.model.RegisterRequestBody
import com.example.culinairy.repository.AuthRepository
import com.example.culinairy.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val name = binding.namaEt.text.toString().trim()
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()
            val address = binding.posisiEt.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && address.isNotEmpty()) {
                register(name, email, password, address)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(name: String, email: String, password: String, address: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = AuthRepository().register(RegisterRequestBody(name, email, password, address))
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Register success.", Toast.LENGTH_SHORT).show()
                    Log.d("Register Activity - Register", "Register success")
                    navigateToLogin()
                } else {
                    Toast.makeText(this@RegisterActivity, "Register failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                    Log.e("Register Activity - Register", "Register failed. Please check your credentials.")
                }
            } catch (e: HttpException) {
                Toast.makeText(this@RegisterActivity, "Register failed. ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Register Activity - Register", "Register failed. Http exception: ", e)
            } catch (e: Throwable) {
                Toast.makeText(this@RegisterActivity, "Register failed. ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Register Activity - Register", "Register failed. Throwable: ", e)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
