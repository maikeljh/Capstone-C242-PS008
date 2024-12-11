package com.example.culinairy.model

data class TransactionResponse(
    val status: String,
    val message: String,
    val error: String?,
    val error_code: String?,
    val data: Transaction
)
