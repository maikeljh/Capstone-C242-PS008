package com.example.culinairy.repository

import retrofit2.Response
import com.example.culinairy.model.*
import com.example.culinairy.services.AuthService
import com.example.culinairy.services.RetrofitInstance

class AuthRepository {

    private val authService: AuthService = RetrofitInstance.authService

    suspend fun login(loginBody: LoginRequestBody): Response<LoginResponse> {
        return authService.login(loginBody)
    }

    suspend fun register(registerBody: RegisterRequestBody): Response<RegisterResponse> {
        return authService.register(registerBody)
    }

    suspend fun getUser(token: String): Response<RegisterResponse> {
        return authService.getUser(token)
    }

    suspend fun updateUser(token: String, updateUserBody: UpdateUserRequestBody): Response<RegisterResponse> {
        return authService.updateUser(token, updateUserBody)
    }
}