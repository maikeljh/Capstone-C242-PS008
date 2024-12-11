package com.example.culinairy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.culinairy.model.Transactions.Transaction
import com.example.culinairy.repository.AuthRepository
import com.example.culinairy.repository.TransactionRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val transactionRepository = TransactionRepository()

    private val _name = MutableLiveData<String>("")
    val name: LiveData<String> get() = _name

    private val _omset = MutableLiveData<String>("")
    val omset: LiveData<String> get() = _omset

    private val _jumlahOrder = MutableLiveData<String>("0")
    val jumlahOrder: LiveData<String> get() = _jumlahOrder

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _transactionsResult = MutableLiveData<TransactionRepository.Result<List<Transaction>>>()
    val transactionsResult: LiveData<TransactionRepository.Result<List<Transaction>>> get() = _transactionsResult

    private var activeTasks = 0
        set(value) {
            field = value
            _isLoading.postValue(value > 0)
        }

    fun fetchUser(token: String) {
        activeTasks++
        viewModelScope.launch {
            try {
                val response = repository.getUser(token)
                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        _name.postValue(it.user.name)
                    }
                } else {
                    _error.postValue("Failed to fetch user: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            } finally {
                activeTasks--
            }
        }
    }

    fun loadTransactions(token: String) {
        activeTasks++
        viewModelScope.launch {
            _transactionsResult.postValue(TransactionRepository.Result.Loading)
            val result = transactionRepository.fetchAllTransactions(token)

            if (result is TransactionRepository.Result.Success) {
                val transactions = result.data
                val totalOmset = transactions.sumOf { it.total_price }
                val totalOrders = transactions.size
                val formattedOmset = NumberFormat.getNumberInstance(Locale.US).format(totalOmset)

                _omset.postValue(formattedOmset)
                _jumlahOrder.postValue(totalOrders.toString())
            } else if (result is TransactionRepository.Result.Error) {
                _omset.postValue("0")
                _jumlahOrder.postValue("0")
            }
            _transactionsResult.postValue(result)
            activeTasks--
        }
    }
}

