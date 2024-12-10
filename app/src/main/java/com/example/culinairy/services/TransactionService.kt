package com.example.culinairy.services

import com.example.culinairy.model.Transaction.OCRResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TransactionService {
    @Multipart
    @POST("api/v1/transactions/ocr")
    suspend fun ocr(@Header("Authorization") token: String, @Part image: MultipartBody.Part): Response<OCRResponse>
}