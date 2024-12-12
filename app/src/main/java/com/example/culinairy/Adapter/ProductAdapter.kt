package com.example.culinairy.Adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R
import com.example.culinairy.databinding.ProductCardBinding
import com.example.culinairy.model.product.Product

class ProductAdapter(
    private var productList: List<Product>,
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
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

        // Bind data to views using ViewBinding
        holder.binding.itemText.text = product.product_name
        holder.binding.priceText.text = product.price.toString()

        // Edit button functionality
//        holder.binding.editButton.setOnClickListener {
//            onEditClick(product)
//        }
        // Edit button functionality
        holder.binding.editButton.setOnClickListener {
            onEditClick(product)
        }

        // Delete button functionality
        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(product)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun updateData(newProducts: List<Product>) {
        productList = newProducts
        filteredList = productList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            productList
        } else {
            productList.filter { it.product_name.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}


