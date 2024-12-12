package com.example.culinairy.model.product

data class GetProductResponse (
    val status: String,
    val message: String,
    val data: Product
)