package com.example.culinairy.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.repository.ProductOnlyRepository
import com.example.culinairy.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productOnlyRepository: ProductOnlyRepository) : ViewModel() {

    // LiveData for observing products list
    private val _products = MutableLiveData<GetProductsResponse?>()
    val products: LiveData<GetProductsResponse?> get() = _products

    // LiveData for observing error message
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Fetch products with the authorization token
    fun fetchProducts(token: String) {
        viewModelScope.launch {
            try {
                // Make the API call via the repository
                val response = productOnlyRepository.getProducts(token)

                // Log the response for debugging
                if (response.isSuccessful) {
                    _products.value = response.body() // Update LiveData with fetched products
                    Log.d("ProductViewModel", "Fetched products: ${response.body()}")
                } else {
                    _error.value = "Error: ${response.message()}" // Set error message if not successful
                    Log.e("ProductViewModel", "Error fetching products: ${response.message()}")
                }
            } catch (exception: Exception) {
                // Handle network or other exceptions
                _error.value = "Exception: ${exception.localizedMessage}"
                Log.e("ProductViewModel", "Exception: ${exception.localizedMessage}")
            }
        }
    }
}

