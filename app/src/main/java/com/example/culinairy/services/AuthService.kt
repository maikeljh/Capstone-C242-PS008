package com.example.culinairy.services

import com.example.culinairy.model.auth.LoginGoogleRequestBody
import com.example.culinairy.model.auth.RegisterRequestBody
import com.example.culinairy.model.auth.LoginRequestBody
import com.example.culinairy.model.auth.RegisterResponse
import com.example.culinairy.model.auth.LoginResponse
import com.example.culinairy.model.auth.UpdateUserRequestBody
import retrofit2.http.*
import retrofit2.Response

interface AuthService {

    // login
    @POST("api/v1/auth/login")
    suspend fun login(@Body loginBody: LoginRequestBody): Response<LoginResponse>

    // register
    @POST("api/v1/auth/register")
    suspend fun register(@Body registerBody: RegisterRequestBody): Response<RegisterResponse>

    // login google
    @POST("api/v1/auth/login/google")
    suspend fun loginGoogle(@Body loginGoogleBody: LoginGoogleRequestBody): Response<LoginResponse>

    // get user
    @GET("api/v1/auth/user")
    suspend fun getUser(@Header("Authorization") token: String): Response<RegisterResponse>

    // update user
    @PUT("api/v1/auth/user")
    suspend fun updateUser(@Header("Authorization") token: String, @Body updateUserBody: UpdateUserRequestBody): Response<RegisterResponse>
}