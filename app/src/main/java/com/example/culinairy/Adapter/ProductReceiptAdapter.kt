package com.example.culinairy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R
import com.example.culinairy.model.Product

class ProductReceiptAdapter(
    private val productList: List<Product>
) : RecyclerView.Adapter<ProductReceiptAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.itemText)
        private val priceText: TextView = itemView.findViewById(R.id.priceText)
        private val quantityText: TextView = itemView.findViewById(R.id.quantityText)
        private val totalPriceText: TextView = itemView.findViewById(R.id.totalPriceText)

        fun bind(product: Product) {
            itemText.text = product.name
            priceText.text = "Rp${product.price}"
            quantityText.text = "${product.quantity}x"
            totalPriceText.text = "Rp${product.totalPrice}"
        }
    }
}