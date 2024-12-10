package com.example.culinairy.services

import com.example.culinairy.model.RegisterRequestBody
import com.example.culinairy.model.LoginRequestBody
import com.example.culinairy.model.RegisterResponse
import com.example.culinairy.model.LoginResponse
import retrofit2.http.*
import retrofit2.Response

interface AuthService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body loginBody: LoginRequestBody): Response<LoginResponse>

    @POST("api/v1/auth/register")
    suspend fun register(@Body registerBody: RegisterRequestBody): Response<RegisterResponse>
}