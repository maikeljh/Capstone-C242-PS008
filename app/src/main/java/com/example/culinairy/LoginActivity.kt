package com.example.culinairy
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.culinairy.databinding.ActivityLoginBinding
import com.example.culinairy.ui.login.LoginViewModel
import com.example.culinairy.utils.TokenManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

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
            setButtonsEnabled(false)
            binding.loadingAnimation.visibility = android.view.View.VISIBLE
            binding.darkOverlay.visibility = android.view.View.VISIBLE
            googleSignIn()
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
            is LoginViewModel.LoginState.Loading -> {
                binding.loadingAnimation.visibility = android.view.View.VISIBLE
                binding.darkOverlay.visibility = android.view.View.VISIBLE
            }
        }
    }

    // Google Sign-In handler
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed with status code: ${e.statusCode}", e)
            Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)

    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account == null) {
            Toast.makeText(this, "Google Sign-In failed: Account is null.", Toast.LENGTH_SHORT).show()
            setButtonsEnabled(true)
            binding.loadingAnimation.visibility = android.view.View.GONE
            binding.darkOverlay.visibility = android.view.View.GONE
            return
        }

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
                setButtonsEnabled(true)
                binding.loadingAnimation.visibility = android.view.View.GONE
                binding.darkOverlay.visibility = android.view.View.GONE
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
        if (isEnabled) {
            binding.loadingAnimation.visibility = android.view.View.GONE
            binding.darkOverlay.visibility = android.view.View.GONE
        }
    }
}
