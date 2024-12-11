package com.example.culinairy.services

import com.example.culinairy.model.Transaction
import com.example.culinairy.model.TransactionAllResponse
import com.example.culinairy.model.TransactionResponse
import retrofit2.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TransactionService {
    // GET: Get All Transactions
    @GET("/api/v1/transactions")
    suspend fun getAllTransactions(): Response<TransactionAllResponse>

    // GET: Get Transaction by ID
    @GET("/api/v1/transactions/{id}")
    suspend fun getTransactionById(@Path("id") id: String): Response<TransactionResponse>

}