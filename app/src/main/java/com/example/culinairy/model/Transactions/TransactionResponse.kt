package com.example.culinairy.model.Transactions

data class TransactionTopProductsResponse(
    val status: String,
    val message: String,
    val error: String? = null,
    val error_code: String? = null,
    val data: List<TopProduct>
)

data class TopProduct(
    val product_id: String,
    val product_name: String,
    val total_quantity: Int
)