package com.example.culinairy.model

data class TransactionProduct(
    val product_id: String,
    val product_name: String,
    val price_per_unit: Int,
    val quantity: Int,
    val total_price: Int
)
