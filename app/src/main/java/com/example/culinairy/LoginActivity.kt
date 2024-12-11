package com.example.culinairy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.culinairy.databinding.ActivityLoginBinding
import com.example.culinairy.ui.login.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        oneTapClient = Identity.getSignInClient(this)

        // login
        binding.loginButton.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passEt.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                setButtonsEnabled(false)
                viewModel.login(this@LoginActivity, email = email, password = password) { state ->
                    handleLoginState(state)
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // register
        binding.registerButton.setOnClickListener {
            navigateToRegister()
        }

        // login google
        binding.googleButton.setOnClickListener {
            setButtonsEnabled(false)
            binding.loadingAnimation.visibility = android.view.View.VISIBLE
            binding.darkOverlay.visibility = android.view.View.VISIBLE
            startGoogleSignIn()
        }
    }

    private fun startGoogleSignIn() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.DEFAULT_WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                val intent = result.pendingIntent.intentSender
                googleSignInLauncher.launch(IntentSenderRequest.Builder(intent).build())
            }
            .addOnFailureListener { e ->
                Log.e("GoogleSignIn", "One Tap Sign-In failed: ${e.message}", e)
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
                setButtonsEnabled(true)
                binding.loadingAnimation.visibility = android.view.View.GONE
                binding.darkOverlay.visibility = android.view.View.GONE
            }
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                firebaseAuthWithGoogle(idToken)
            } else {
                Toast.makeText(this, "Google Sign-In failed: ID Token is null.", Toast.LENGTH_SHORT).show()
                setButtonsEnabled(true)
                binding.loadingAnimation.visibility = android.view.View.GONE
                binding.darkOverlay.visibility = android.view.View.GONE
            }
        } else {
            Toast.makeText(this, "Google Sign-In cancelled.", Toast.LENGTH_SHORT).show()
            setButtonsEnabled(true)
            binding.loadingAnimation.visibility = android.view.View.GONE
            binding.darkOverlay.visibility = android.view.View.GONE
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val firebaseIdToken = tokenTask.result?.token

                            // login google
                            viewModel.login(this@LoginActivity, googleToken = firebaseIdToken, google = true) { state ->
                                handleLoginState(state)
                            }
                            navigateToMain()
                        } else {
                            Log.e("TOKEN", "Error fetching Firebase ID Token", tokenTask.exception)
                            Toast.makeText(this, "Failed to retrieve Firebase ID Token.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
                setButtonsEnabled(true)
                binding.loadingAnimation.visibility = android.view.View.GONE
                binding.darkOverlay.visibility = android.view.View.GONE
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