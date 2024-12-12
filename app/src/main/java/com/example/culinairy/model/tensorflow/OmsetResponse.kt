package com.example.culinairy.model.tensorflow
import com.google.gson.annotations.SerializedName
import java.util.Date

data class OmsetResponse(
    val status: String,
    val message: String,
    val error: String? = null,
    val error_code: String? = null,
    val data: OmsetData
)

data class OmsetData(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("current_data")
    val currentData: List<DataPoint>,
    @SerializedName("predictions")
    val predictions: List<Prediction>
)

data class DataPoint(
    val timestamp: Date,
    val price: Long
)

data class Prediction(
    val timestamp: Date,
    val price: Double
)