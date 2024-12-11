package com.example.culinairy.repository

import com.example.culinairy.model.transaction.OCRResponse
import com.example.culinairy.services.RetrofitInstance
import com.example.culinairy.services.TransactionService
import okhttp3.MultipartBody
import retrofit2.Response

class TransactionRepository {
    private val transactionService: TransactionService = RetrofitInstance.transactionService

    suspend fun ocr(token: String, ocrBody: MultipartBody.Part): Response<OCRResponse> {
        return transactionService.ocr(token, ocrBody)
    }
}