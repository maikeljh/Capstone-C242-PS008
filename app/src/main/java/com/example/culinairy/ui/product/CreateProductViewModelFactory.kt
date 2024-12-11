package com.example.culinairy.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.culinairy.repository.ProductOnlyRepository

class CreateProductViewModelFactory(
    private val productOnlyRepository: ProductOnlyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateProductViewModel::class.java)) {
            return CreateProductViewModel(productOnlyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

