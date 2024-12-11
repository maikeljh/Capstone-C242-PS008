package com.example.culinairy.services

import com.example.culinairy.model.product.GetProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProductService {
    // Get all products
    @GET("api/v1/products")
    suspend fun getProducts(@Header("Authorization") token: String): Response<GetProductsResponse>
}