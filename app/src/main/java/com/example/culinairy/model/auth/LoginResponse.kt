package com.example.culinairy.model.auth

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String,
    val exp: Long
)
