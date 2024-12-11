package com.example.culinairy.model.transaction

data class OCRData(
    val user_id: String,
    val timestamp: String,
    val items: List<OCRItem>,
    val total_price: Int
)
