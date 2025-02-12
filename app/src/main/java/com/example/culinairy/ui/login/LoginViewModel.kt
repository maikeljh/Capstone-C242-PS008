package com.example.culinairy.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.auth.LoginGoogleRequestBody
import com.example.culinairy.model.auth.LoginRequestBody
import com.example.culinairy.repository.AuthRepository
import com.example.culinairy.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    sealed class LoginState {
        data object Success : LoginState()
        data object Failure : LoginState()
        data object InvalidCredentials : LoginState()
        data class Error(val message: String) : LoginState()
        data object Loading : LoginState()
    }

    // login & google login
    fun login(
        context: Context,
        email: String? = null,
        password: String? = null,
        google: Boolean = false,
        googleToken: String? = null,
        onResult: (LoginState) -> Unit
    ) {
        viewModelScope.launch {
            onResult(LoginState.Loading)
            try {
                val response = withContext(Dispatchers.IO) {
                    if (google && googleToken != null) {
                        AuthRepository().loginGoogle(LoginGoogleRequestBody(googleToken))
                    } else {
                        AuthRepository().login(LoginRequestBody(email!!, password!!))
                    }
                }

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    val exp = response.body()?.exp

                    // save token and exp
                    if (token != null && exp != null) {
                        TokenManager.saveExpToken(context, exp)
                        TokenManager.saveEncryptedToken(context, token)
                        onResult(LoginState.Success)
                    } else {
                        onResult(LoginState.InvalidCredentials)
                    }
                } else {
                    onResult(LoginState.InvalidCredentials)
                }
            } catch (e: HttpException) {
                onResult(LoginState.Error("Http exception: ${e.message}"))
            } catch (e: Throwable) {
                onResult(LoginState.Error("An error occurred: ${e.message}"))
            }
        }
    }
}
