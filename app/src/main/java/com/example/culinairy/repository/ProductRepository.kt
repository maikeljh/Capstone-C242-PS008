package com.example.culinairy.repository

import com.example.culinairy.model.product.GetProductResponse
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.services.ProductService
import com.example.culinairy.services.RetrofitInstance
import retrofit2.Response

class ProductRepository {
    private val productService: ProductService = RetrofitInstance.productService

    suspend fun getProducts(token: String): Response<GetProductsResponse> {
        return productService.getProducts(token)
    }
    suspend fun createProduct(token: String, productRequest: ProductRequest): Response<GetProductResponse> {
        return productService.createProduct(token, productRequest)
    }
}

