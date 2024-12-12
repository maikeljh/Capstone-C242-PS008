package com.example.culinairy.repository

import com.example.culinairy.model.product.GetProductResponse
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.services.ProductService
import com.example.culinairy.services.RetrofitInstance
import retrofit2.Response

class ProductRepository {
    private val productService: ProductService = RetrofitInstance.productService

    // Get all products
    suspend fun getProducts(token: String): Response<GetProductsResponse> {
        return productService.getProducts(token)
    }

    // Create a new product
    suspend fun createProduct(token: String, productRequest: ProductRequest): Response<GetProductResponse> {
        return productService.createProduct(token, productRequest)
    }

    // Update an existing product
    suspend fun updateProduct(token: String, productId: String, productRequest: ProductRequest): Response<GetProductResponse> {
        return productService.updateProduct(token, productId, productRequest)
    }
}

