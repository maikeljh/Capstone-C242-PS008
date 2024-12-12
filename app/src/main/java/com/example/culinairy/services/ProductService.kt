package com.example.culinairy.services

import com.example.culinairy.model.product.GetProductResponse
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.model.product.ProductRequest
import retrofit2.Response
import retrofit2.http.*

interface ProductService {
    // Get all products
    @GET("api/v1/products")
    suspend fun getProducts(@Header("Authorization") token: String): Response<GetProductsResponse>

    @POST("api/v1/products")
    suspend fun createProduct(@Header("Authorization") token: String, @Body productRequest: ProductRequest): Response<GetProductResponse>

    @PUT("/api/v1/products/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body productRequest: ProductRequest
    ): Response<GetProductResponse>

    // DELETE: Delete a transaction
    @DELETE("/api/v1/products/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Void>
}