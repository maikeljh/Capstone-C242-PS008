package com.example.culinairy.model.Transactions

data class Transaction(
    val transaction_id: String,
    val timestamp: String,
    val total_price: Int,
    val items: List<TransactionProduct>
)
