package com.example.culinairy.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.Transactions.Transaction
import com.example.culinairy.model.Transactions.TransactionAllResponse
import com.example.culinairy.repository.TransactionRepository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    // Initialize TransactionRepository directly
    private val repository = TransactionRepository()

    private val _transactionResult = MutableLiveData<TransactionRepository.Result<Transaction>>()
    val transactionResult: LiveData<TransactionRepository.Result<Transaction>> get() = _transactionResult

    private val _transactionsResult = MutableLiveData<TransactionRepository.Result<TransactionAllResponse>>()
    val transactionsResult: LiveData<TransactionRepository.Result<TransactionAllResponse>> get() = _transactionsResult

    fun loadTransactionById(id: String) {
        viewModelScope.launch {
            _transactionResult.postValue(TransactionRepository.Result.Loading) // Post Loading state
            val result = repository.fetchTransactionById(id)
            _transactionResult.postValue(result) // Post Success or Error state
        }
    }

    fun loadTransactions(token: String) {
        viewModelScope.launch {
            _transactionsResult.postValue(TransactionRepository.Result.Loading)
            val result = repository.fetchAllTransactions(token)
            _transactionsResult.postValue(result)
        }
    }
}