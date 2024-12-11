package com.example.culinairy.services

import android.content.Context
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.culinairy.BuildConfig
import com.example.culinairy.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val transactionService: TransactionService by lazy {
        retrofit.create(TransactionService::class.java)
    }
}