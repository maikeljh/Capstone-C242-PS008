package com.example.culinairy.repository

import com.example.culinairy.model.product.GetProductResponse
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.services.ProductService
import retrofit2.Response

class ProductOnlyRepository(private val productService: ProductService) {

    // Example method to fetch products
    suspend fun getProducts(token: String) = productService.getProducts(token)

    suspend fun createProduct(token: String, productRequest: ProductRequest): Response<GetProductResponse> {
        return productService.createProduct(token, productRequest)
    }
}