package com.example.culinairy

import Item
import ListTransactionAdapter
import TransactionItem
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_transaction)

        supportActionBar?.apply {
            title = "Daftar Semua Transaksi"
            setDisplayHomeAsUpEnabled(true)
        }

        val transactionList = listOf(
            TransactionItem(
                title = "Payment from #1132",
                date = "Hari ini, 24 November 2024 - 20:13",
                totalPrice = "Rp250.000",
                items = listOf(
                    Item(name = "Beras 15kg", price = "Rp150.000", quantity = 1, total = "Rp150.000"),
                    Item(name = "Telor 1kg", price = "Rp20.000", quantity = 5, total = "Rp100.000")
                )
            ),
            TransactionItem(
                title = "Payment from #1133",
                date = "Kemarin, 23 November 2024 - 18:30",
                totalPrice = "Rp230.000",
                items = listOf(
                    Item(name = "Minyak Goreng 2L", price = "Rp50.000", quantity = 2, total = "Rp100.000"),
                    Item(name = "Gula Pasir 1kg", price = "Rp65.000", quantity = 2, total = "Rp130.000")
                )
            )
        )

        val recyclerView: RecyclerView = findViewById(R.id.transaction_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ListTransactionAdapter(transactionList)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
