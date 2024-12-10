package com.example.culinairy.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.culinairy.LoginActivity
import java.security.KeyStore

object LogoutManager {
    fun logout(context: Context) {
        clearSessionData(context)

        navigateToSignIn(context)
    }

    private fun clearSessionData(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("jwt_token")
        editor.remove("exp")
        editor.apply()

        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            keyStore.deleteEntry("MySecretKey")
        } catch (e: Exception) {
            Log.e("Logout Utils - Clear Session data", "Error deleting secret key from keystore: ${e.message}", e)
        }
    }

    private fun navigateToSignIn(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}