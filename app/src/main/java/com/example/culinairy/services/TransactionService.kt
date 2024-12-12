package com.example.culinairy.services

import com.example.culinairy.model.transaction.OCRResponse
import com.example.culinairy.model.Transactions.TransactionAllResponse
import com.example.culinairy.model.Transactions.TransactionResponse
import com.example.culinairy.model.transaction.CreateTransactionBodyRequest
import com.example.culinairy.model.transaction.CreateTransactionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Part

interface TransactionService {
    // GET: Get All Transactions
    @GET("/api/v1/transactions")
    suspend fun getAllTransactions(@Header("Authorization") token: String): Response<TransactionAllResponse>

    // GET: Get Transaction by ID
    @GET("/api/v1/transactions/{id}")
    suspend fun getTransactionById(@Path("id") id: String): Response<TransactionResponse>

    // POST: OCR
    @Multipart
    @POST("api/v1/transactions/ocr")
    suspend fun ocr(@Header("Authorization") token: String, @Part image: MultipartBody.Part): Response<OCRResponse>

    // POST: Create a new transaction
    @POST("/api/v1/transactions")
    suspend fun createTransaction(@Header("Authorization") token: String, @Body transaction: CreateTransactionBodyRequest): Response<CreateTransactionResponse>

    // DELETE: Delete a transaction
    @DELETE("/api/v1/transactions/{id}")
    suspend fun deleteTransaction(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Void>
}