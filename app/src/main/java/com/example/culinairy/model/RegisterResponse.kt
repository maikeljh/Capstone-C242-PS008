package com.example.culinairy.model

data class RegisterResponse(
    val status: String,
    val message: String,
    val error: String,
    val user: User
)