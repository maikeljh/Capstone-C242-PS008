package com.example.culinairy.model.transaction

data class TransactionProduct(
    var name: String,
    var quantity: Int,
    var price: Int,
    var totalPrice: Int,
) {
    fun copyWithNewQuantity(newQuantity: Int): TransactionProduct {
        return this.copy(quantity = newQuantity, totalPrice = price * newQuantity)
    }
}
