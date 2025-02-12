package com.example.culinairy.repository

import com.example.culinairy.model.transaction.OCRResponse
import com.example.culinairy.model.Transactions.TransactionAllResponse
import com.example.culinairy.model.Transactions.TransactionTopProductsResponse
import com.example.culinairy.model.tensorflow.OmsetResponse
import com.example.culinairy.model.transaction.CreateTransactionBodyRequest
import com.example.culinairy.model.transaction.CreateTransactionResponse
import com.example.culinairy.services.RetrofitInstance
import com.example.culinairy.services.TransactionService
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class TransactionRepository {

    // Initialize TransactionService directly inside the class
    private val transactionService: TransactionService = RetrofitInstance.transactionService

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }

    suspend fun ocr(token: String, ocrBody: MultipartBody.Part): Response<OCRResponse> {
        return transactionService.ocr(token, ocrBody)
    }

    suspend fun createTransaction(token: String, transaction: CreateTransactionBodyRequest): Response<CreateTransactionResponse> {
        return transactionService.createTransaction(token, transaction)
    }

    suspend fun fetchTopProducts(token: String): Result<TransactionTopProductsResponse> {
        return try {
            val response = transactionService.getTop5Products(token)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    Result.Success(body)
                } else {
                    Result.Error(body?.message ?: "Unknown error")
                }
            } else {
                Result.Error("HTTP error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Exception: ${e.message}")
        }
    }

    suspend fun fetchAllTransactions(token: String): Result<TransactionAllResponse> {
        return try {
            val response = transactionService.getAllTransactions(token) // Pass the token
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    Result.Success(body)
                } else {
                    Result.Error(body?.message ?: "Unknown error")
                }
            } else {
                Result.Error("HTTP error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Exception: ${e.message}")
        }
    }

    suspend fun deleteTransaction(token: String, id: String): Result<Unit> {
        return try {
            val response = transactionService.deleteTransaction(token, id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Failed to delete transaction: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    // Get omset
    suspend fun getOmset(id: String): Result<OmsetResponse> {
        return try {
            val response = transactionService.getOmset(id)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: HttpException) {
            Result.Error("HTTP Exception: ${e.message}")
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Unexpected Error: ${e.message}")
        }
    }
}