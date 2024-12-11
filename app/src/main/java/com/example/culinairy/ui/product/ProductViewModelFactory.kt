package com.example.culinairy.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.culinairy.repository.ProductOnlyRepository
import com.example.culinairy.repository.ProductRepository
import com.example.culinairy.services.RetrofitInstance

class ProductViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            // Create a ProductOnlyRepository with the ProductService from RetrofitInstance
            val productOnlyRepository = ProductOnlyRepository(RetrofitInstance.productService)
            return ProductViewModel(productOnlyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
