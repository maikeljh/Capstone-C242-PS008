package com.example.culinairy.model.transaction

data class CreateTransactionBodyRequest(
    val items: List<Item>
) {
    data class Item(
        val product_id: String,
        val quantity: Int
    )
}
