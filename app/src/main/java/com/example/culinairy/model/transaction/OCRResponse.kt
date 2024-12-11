package com.example.culinairy.model.transaction

data class OCRResponse(
    val status: String,
    val message: String,
    val data: OCRData
)