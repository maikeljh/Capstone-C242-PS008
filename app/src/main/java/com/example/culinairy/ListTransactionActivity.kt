package com.example.culinairy

import Item
import ListTransactionAdapter
import TransactionItem
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.culinairy.databinding.ActivityListTransactionBinding
import com.example.culinairy.repository.TransactionRepository
import com.example.culinairy.utils.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ListTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListTransactionBinding
    private lateinit var adapter: ListTransactionAdapter
    private val transactionRepository = TransactionRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Daftar Semua Transaksi"
            setDisplayHomeAsUpEnabled(true)
        }

        setupRecyclerView(emptyList())

        val token = TokenManager.retrieveToken(this)
        if (token != null) {
            loadTransactions(token)
        } else {
            Toast.makeText(this, "Invalid token. Cannot fetch transactions.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(transactions: List<TransactionItem>) {
        adapter = ListTransactionAdapter(transactions, { transaction ->
            val token = TokenManager.retrieveToken(this)
            if (token != null) {
                deleteTransaction(token, transaction.transactionId)
            } else {
                Toast.makeText(this, "Invalid token. Cannot delete transaction.", Toast.LENGTH_SHORT).show()
            }
        }, this)
        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter = adapter
    }

    private fun loadTransactions(token: String) {
        setLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = transactionRepository.fetchAllTransactions(token)
                withContext(Dispatchers.Main) {
                    setLoading(false)

                    when (result) {
                        is TransactionRepository.Result.Success -> {
                            val transactions = result.data.data.transactions
                                .sortedByDescending { it.timestamp }
                                .map { transaction ->
                                    TransactionItem(
                                        title = "Payment from #${transaction.transaction_id}",
                                        date = formatTimestamp(transaction.timestamp),
                                        totalPrice = "Rp${formatNumber(transaction.total_price)}",
                                        items = transaction.items.map { item ->
                                            Item(
                                                name = item.product_name,
                                                price = "Rp${formatNumber(item.price_per_unit)}",
                                                quantity = item.quantity,
                                                total = "Rp${formatNumber(item.total_price)}"
                                            )
                                        },
                                        transactionId = transaction.transaction_id
                                    )
                                }

                            adapter.updateData(transactions)
                        }
                        is TransactionRepository.Result.Error -> {
                            Toast.makeText(this@ListTransactionActivity, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                        }
                        TransactionRepository.Result.Loading -> {
                            Toast.makeText(this@ListTransactionActivity, "Loading transactions...", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    setLoading(false)
                    Toast.makeText(this@ListTransactionActivity, "Failed to load transactions: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteTransaction(token: String, transactionId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = transactionRepository.deleteTransaction(token, transactionId)
                withContext(Dispatchers.Main) {
                    if (response is TransactionRepository.Result.Success) {
                        Toast.makeText(
                            this@ListTransactionActivity,
                            "Transaction deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadTransactions(token)
                    } else if (response is TransactionRepository.Result.Error) {
                        Toast.makeText(
                            this@ListTransactionActivity,
                            "Failed to delete transaction: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ListTransactionActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun formatNumber(value: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(value)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loadingAnimation.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.darkOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun formatTimestamp(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            timestamp
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
