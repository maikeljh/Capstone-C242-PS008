package com.example.culinairy.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R
import com.example.culinairy.model.product.Product

class ProductAdapter(
    private val productList: List<Product>,
    private val onEditClick: (Product) -> Unit // Callback for edit button clicks
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)

        // Set click listener for the edit button
        holder.editButton.setOnClickListener {
            onEditClick(product) // Trigger callback with the current product
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.itemText)
        private val priceText: TextView = itemView.findViewById(R.id.priceText)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            itemText.text = product.product_name
            priceText.text = "Rp${product.price}"
        }
    }
}