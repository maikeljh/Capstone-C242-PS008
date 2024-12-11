package com.example.culinairy.model.TransactionOCR

data class OCRResponse(
    val status: String,
    val message: String,
    val data: Data
)

data class Data(
    val user_id: String,
    val timestamp: String,
    val items: List<Item>,
    val total_price: Int
)

data class Item(
    val product_name: String,
    val quantity: Int,
    val price_per_unit: Int,
    val total_price: Int,
    val product_id: String
)
