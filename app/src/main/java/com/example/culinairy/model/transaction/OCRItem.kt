package com.example.culinairy.model.transaction

data class OCRItem(
    val product_name: String,
    val quantity: Int,
    val price_per_unit: Int,
    val total_price: Int,
    val product_id: String
)
