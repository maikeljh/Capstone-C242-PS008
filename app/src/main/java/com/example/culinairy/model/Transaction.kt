package com.example.culinairy.model

data class Transaction(
    val transaction_id: String,
    val timestamp: String,
    val total_price: Int,
    val items: List<TransactionProduct>
)
