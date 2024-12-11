package com.example.culinairy.model.auth

data class RegisterRequestBody(
    val name: String,
    val email: String,
    val password: String,
    val address: String
)
