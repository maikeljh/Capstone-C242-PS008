package com.example.culinairy.Adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.databinding.ProductCardBinding
import com.example.culinairy.model.product.Product

class ProductAdapter(
    private var productList: List<Product>,
    private val onEditClick: (Product) -> Unit // Pass a lambda for handling edit clicks
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var filteredList: List<Product> = productList

    class ProductViewHolder(val binding: ProductCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = filteredList[position]
        holder.binding.itemText.text = product.product_name
        holder.binding.priceText.text = product.price.toString()

        // Set up the edit button click listener
        holder.binding.editButton.setOnClickListener {
            onEditClick(product)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateData(newProducts: List<Product>) {
        productList = newProducts
        filteredList = productList
        notifyDataSetChanged()
    }
    fun filter(query: String) {
//        Log.d("Message", "Filtered")
        filteredList = if (query.isEmpty()) {
            Log.d("Message", "Empty Query")
            productList
        } else {
            Log.d("Message", "Filtered Query")
            productList.filter { it.product_name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}

