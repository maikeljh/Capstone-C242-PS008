package com.example.culinairy.ui.capture_ocr_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.model.transaction.CreateTransactionBodyRequest
import com.example.culinairy.model.transaction.CreateTransactionResponse
import com.example.culinairy.repository.ProductRepository
import com.example.culinairy.repository.TransactionRepository
import kotlinx.coroutines.launch

class CaptureReceiptResultViewModel : ViewModel() {
    private val productRepository = ProductRepository()
    private val transactionRepository = TransactionRepository()

    private val _getProductsResponse = MutableLiveData<GetProductsResponse?>()
    val getProductsResponse: LiveData<GetProductsResponse?> = _getProductsResponse

    private val _createTransactionResponse = MutableLiveData<CreateTransactionResponse?>()
    val createTransactionResponse: LiveData<CreateTransactionResponse?> = _createTransactionResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getProducts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = productRepository.getProducts(token)
                if (response.isSuccessful) {
                    _getProductsResponse.value = response.body()
                    _errorMessage.value = null
                } else {
                    _getProductsResponse.value = null
                    _errorMessage.value = response.errorBody()?.string() ?: "Unknown error"
                }
            } catch (e: Exception) {
                _getProductsResponse.value = null
                _errorMessage.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTransaction(token: String, transaction: CreateTransactionBodyRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = transactionRepository.createTransaction(token, transaction)
                if (response.isSuccessful) {
                    _createTransactionResponse.value = response.body()
                    _errorMessage.value = null
                } else {
                    _createTransactionResponse.value = null
                    _errorMessage.value = response.errorBody()?.string() ?: "Unknown error"
                }
            } catch (e: Exception) {
                _createTransactionResponse.value = null
                _errorMessage.value = e.message ?: "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}