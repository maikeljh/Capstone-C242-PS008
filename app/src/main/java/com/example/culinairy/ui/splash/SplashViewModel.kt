package com.example.culinairy.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.culinairy.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SplashViewModel : ViewModel() {

    // check jwt token
    suspend fun isTokenValid(context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            TokenManager.checkToken(context)
        }
    }
}