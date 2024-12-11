package com.example.culinairy.model.Transactions

data class TransactionAllResponse(
    val status: String,
    val message: String,
    val error: String?,
    val error_code: String?,
    val data: TransactionsData
)

data class TransactionsData(
    val transactions: List<Transaction>,
    val meta: MetaData
)

data class MetaData(
    val total: Int,
)
