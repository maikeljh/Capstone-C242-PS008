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
    private val onEditClick: (Product) -> Unit // Lambda for edit button clicks
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
        holder.binding.editButton.setOnClickListener {
            val bundle = Bundle().apply {
                // Manually pass arguments using the bundle
                putString("productId", product.product_id)
                putString("productName", product.product_name)
                putInt("productPrice", product.price)
            }
            // Navigate to UpdateProductFragment using the bundle
            it.findNavController().navigate(R.id.navigation_update_product, bundle)
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


