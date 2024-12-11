package com.example.culinairy.model.product

data class ProductRequest(
    val user_id: String,      // User ID to associate the product with
    val product_name: String, // Name of the product
    val price: Int        // Price of the product
)