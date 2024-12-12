package com.example.culinairy.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.product.GetProductResponse
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.repository.ProductOnlyRepository
import kotlinx.coroutines.launch

class CreateProductViewModel(
    private val productOnlyRepository: ProductOnlyRepository
) : ViewModel() {

    private val _createProductResponse = MutableLiveData<GetProductResponse?>()
    val createProductResponse: LiveData<GetProductResponse?> get() = _createProductResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Create product method
    fun createProduct(token: String, productRequest: ProductRequest) {
        viewModelScope.launch {
            try {
                // Correctly call the instance method from the injected repository
                val response = productOnlyRepository.createProduct(token, productRequest)

                // Check if the response is successful
                if (response.isSuccessful) {
                    _createProductResponse.value = response.body() // Update LiveData with response
                } else {
                    _error.value = "Error: ${response.message()}" // Set error message
//                    Log.e("Response: ", "${response}")
//                    Log.e("Token: ", "${token}")
                }
            } catch (exception: Exception) {
                _error.value = "Exception: ${exception.localizedMessage}" // Handle exceptions
            }
        }
    }
}

