package com.example.culinairy.ui.product

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.product.GetProductsResponse
import com.example.culinairy.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    // LiveData for observing products list
    private val _products = MutableLiveData<GetProductsResponse?>()
    val products: LiveData<GetProductsResponse?> get() = _products

    // LiveData for observing error message
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // LiveData for observing delete status
    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> get() = _deleteStatus

    // Fetch products with the authorization token
    fun fetchProducts(token: String) {
        viewModelScope.launch {
            try {
                // Make the API call via the repository
                val response = productRepository.getProducts(token)

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

    // Delete a product
    fun deleteProduct(token: String, productId: String) {
        viewModelScope.launch {
            try {
                val response = productRepository.deleteProduct(token, productId)
                if (response.isSuccessful) {
                    _deleteStatus.value = true // Notify success
                    fetchProducts(token) // Refresh product list after deletion
                } else {
                    _deleteStatus.value = false // Notify failure
                    _error.value = "Failed to delete product: ${response.message()}"
                }
            } catch (e: Exception) {
                _deleteStatus.value = false // Notify failure
                _error.value = "An error occurred: ${e.message}"
            }
        }
    }
}

