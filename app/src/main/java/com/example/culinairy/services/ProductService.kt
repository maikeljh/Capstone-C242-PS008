package com.example.culinairy.services

import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.model.product.ProductRequest
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    // Get all products
    @GET("api/v1/products")
    suspend fun getProducts(@Header("Authorization") token: String): Response<GetProductsResponse>

    @POST("api/v1/products/create")
    suspend fun createProduct(@Body productRequest: ProductRequest): Response<GetProductsResponse>
}