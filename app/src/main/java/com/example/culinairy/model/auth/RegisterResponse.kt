package com.example.culinairy.model.auth

data class RegisterResponse(
    val status: String,
    val message: String,
    val error: String,
    val user: User
)