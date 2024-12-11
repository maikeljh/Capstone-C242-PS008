package com.example.culinairy.repository

import com.example.culinairy.services.ProductService

class ProductOnlyRepository(private val productService: ProductService) {

    // Example method to fetch products
    suspend fun getProducts(token: String) = productService.getProducts(token)
}