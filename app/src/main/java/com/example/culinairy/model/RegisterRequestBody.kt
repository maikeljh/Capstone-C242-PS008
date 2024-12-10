package com.example.culinairy.model

data class RegisterRequestBody(
    val name: String,
    val email: String,
    val password: String,
    val address: String
)
