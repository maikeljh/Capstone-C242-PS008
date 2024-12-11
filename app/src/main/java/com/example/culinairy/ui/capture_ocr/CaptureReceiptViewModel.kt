package com.example.culinairy.ui.capture_ocr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.transaction.OCRResponse
import com.example.culinairy.repository.TransactionRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Response

class CaptureReceiptViewModel : ViewModel() {
    private val repository = TransactionRepository()

    private val _ocrResponse = MutableLiveData<OCRResponse?>()
    val ocrResponse: LiveData<OCRResponse?> = _ocrResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun processReceipt(token: String, ocrBody: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: Response<OCRResponse> = repository.ocr(token, ocrBody)
                if (response.isSuccessful) {
                    _ocrResponse.value = response.body()
                    _errorMessage.value = null
                } else {
                    _ocrResponse.value = null
                    _errorMessage.value = response.errorBody()?.string() ?: "Unknown error"
                }
            } catch (e: Exception) {
                _ocrResponse.value = null
                _errorMessage.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearOcrResponse() {
        _ocrResponse.value = null
    }
}