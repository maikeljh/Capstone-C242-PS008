package com.example.culinairy.model.transaction

data class TransactionProduct(
    var name: String,
    var quantity: Int,
    var price: Int,
    var totalPrice: Int,
) {
}
