package com.example.culinairy.model

data class LoginResponse(
    val status: String,
    val message: String,
    val token: String,
    val exp: Long
)
