package com.example.culinairy.ui.product

import androidx.lifecycle.*
import com.example.culinairy.model.product.GetProductResponse
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.repository.ProductRepository
import kotlinx.coroutines.launch

class UpdateProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _updateProductResponse = MutableLiveData<GetProductResponse?>()
    val updateProductResponse: LiveData<GetProductResponse?> = _updateProductResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun updateProduct(token: String, productId: String, productRequest: ProductRequest) {
        viewModelScope.launch {
            try {
                val response = repository.updateProduct(token, productId, productRequest)
                if (response.isSuccessful) {
                    _updateProductResponse.value = response.body()
                } else {
                    _error.value = "Failed to update product: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}
