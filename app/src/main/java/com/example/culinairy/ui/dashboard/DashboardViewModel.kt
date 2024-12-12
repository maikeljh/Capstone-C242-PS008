package com.example.culinairy.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.Transactions.Transaction
import com.example.culinairy.model.Transactions.TransactionAllResponse
import com.example.culinairy.model.Transactions.TransactionTopProductsResponse
import com.example.culinairy.repository.TransactionRepository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    // Initialize TransactionRepository directly
    private val repository = TransactionRepository()

    // LiveData for all transactions
    private val _transactionsResult = MutableLiveData<TransactionRepository.Result<TransactionAllResponse>>()
    val transactionsResult: LiveData<TransactionRepository.Result<TransactionAllResponse>> get() = _transactionsResult

    // LiveData for top 5 products
    private val _topProductsResult = MutableLiveData<TransactionRepository.Result<TransactionTopProductsResponse>>()
    val topProductsResult: LiveData<TransactionRepository.Result<TransactionTopProductsResponse>>
        get() = _topProductsResult


    // Load all transactions
    fun loadTransactions(token: String) {
        viewModelScope.launch {
            _transactionsResult.postValue(TransactionRepository.Result.Loading)
            val result = repository.fetchAllTransactions(token)
            _transactionsResult.postValue(result)
        }
    }

    fun loadTopProducts(token: String) {
        viewModelScope.launch {
            _topProductsResult.postValue(TransactionRepository.Result.Loading)
            val result = repository.fetchTopProducts(token)
            _topProductsResult.postValue(result)
        }
    }

    // Data model for top product
    data class TopProduct(
        val productName: String,
        val totalQuantity: Int,
    )
}
