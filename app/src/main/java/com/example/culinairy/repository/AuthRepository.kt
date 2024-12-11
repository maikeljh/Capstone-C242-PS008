package com.example.culinairy.repository

import com.example.culinairy.model.auth.LoginGoogleRequestBody
import retrofit2.Response
import com.example.culinairy.model.auth.LoginRequestBody
import com.example.culinairy.model.auth.LoginResponse
import com.example.culinairy.model.auth.RegisterRequestBody
import com.example.culinairy.model.auth.RegisterResponse
import com.example.culinairy.model.auth.UpdateUserRequestBody
import com.example.culinairy.services.AuthService
import com.example.culinairy.services.RetrofitInstance

class AuthRepository {

    private val authService: AuthService = RetrofitInstance.authService

    // login
    suspend fun login(loginBody: LoginRequestBody): Response<LoginResponse> {
        return authService.login(loginBody)
    }

    // register
    suspend fun register(registerBody: RegisterRequestBody): Response<RegisterResponse> {
        return authService.register(registerBody)
    }

    // login google
    suspend fun loginGoogle(loginGoogleBody: LoginGoogleRequestBody): Response<LoginResponse> {
        return authService.loginGoogle(loginGoogleBody)
    }

    // get user
    suspend fun getUser(token: String): Response<RegisterResponse> {
        return authService.getUser(token)
    }

    // update user
    suspend fun updateUser(token: String, updateUserBody: UpdateUserRequestBody): Response<RegisterResponse> {
        return authService.updateUser(token, updateUserBody)
    }
}