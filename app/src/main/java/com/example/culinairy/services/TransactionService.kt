package com.example.culinairy.services

import com.example.culinairy.model.transaction.OCRResponse
import com.example.culinairy.model.Transactions.TransactionAllResponse
import com.example.culinairy.model.Transactions.TransactionResponse
import okhttp3.MultipartBody
import retrofit2.Response
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

    @Multipart
    @POST("api/v1/transactions/ocr")
    suspend fun ocr(@Header("Authorization") token: String, @Part image: MultipartBody.Part): Response<OCRResponse>
}