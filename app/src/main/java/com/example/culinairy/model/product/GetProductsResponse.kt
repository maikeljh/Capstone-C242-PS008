package com.example.culinairy.model.product

data class GetProductsResponse(
    val status: String,
    val message: String,
    val data: ProductData
)
