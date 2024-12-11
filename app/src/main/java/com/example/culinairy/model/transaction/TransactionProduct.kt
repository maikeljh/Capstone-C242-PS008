package com.example.culinairy.model.transaction

data class TransactionProduct(
    var id: String,
    var name: String,
    var quantity: Int,
    var price: Int,
    var totalPrice: Int,
) {
}
