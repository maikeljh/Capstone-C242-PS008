package com.example.culinairy.repository

import com.example.culinairy.model.Transaction
import com.example.culinairy.services.TransactionService

class TransactionRepository(private val transactionService: TransactionService) {

    sealed class Result<out T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }

    suspend fun fetchTransactionById(id: String): Result<Transaction> {
        return try {
            val response = transactionService.getTransactionById(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    Result.Success(body.data)
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

    suspend fun fetchAllTransactions(): Result<List<Transaction>> {
        return try {
            val response = transactionService.getAllTransactions()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.status == "success") {
                    // Access the transactions list
                    Result.Success(body.data.transactions)
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
}