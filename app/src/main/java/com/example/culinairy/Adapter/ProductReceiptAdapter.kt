package com.example.culinairy.Adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R
import com.example.culinairy.model.Product

class ProductReceiptAdapter(
    private val productList: List<Product>
) : RecyclerView.Adapter<ProductReceiptAdapter.ProductViewHolder>() {

    // Variables
    var isEditing: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.itemText.text = product.name
        holder.itemTextEdit.setText(product.name)

        holder.priceText.text = "Rp${product.price}"
        holder.priceTextEdit.setText(product.price.toString())

        holder.quantityText.text = "${product.quantity}x"
        holder.quantityTextEdit.setText(product.quantity.toString())

        holder.totalPriceText.text = "Rp${product.totalPrice}"
        holder.totalPriceTextEdit.setText(product.totalPrice.toString())

        if (isEditing) {
            holder.itemTextEdit.visibility = View.VISIBLE
            holder.priceTextEdit.visibility = View.VISIBLE
            holder.quantityTextEditContainer.visibility = View.VISIBLE
            holder.totalPriceTextEdit.visibility = View.VISIBLE

            holder.itemText.visibility = View.GONE
            holder.priceText.visibility = View.GONE
            holder.quantityText.visibility = View.GONE
            holder.totalPriceText.visibility = View.GONE

            holder.itemTextEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    product.name = s.toString()
                }
            })
            holder.priceTextEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    product.price = s.toString().toIntOrNull() ?: 0
                }
            })
            holder.quantityTextEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    product.quantity = s.toString().toIntOrNull() ?: 0
                }
            })
            holder.totalPriceTextEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    product.totalPrice = s.toString().toIntOrNull() ?: 0
                }
            })
        } else {
            holder.itemText.visibility = View.VISIBLE
            holder.priceText.visibility = View.VISIBLE
            holder.quantityText.visibility = View.VISIBLE
            holder.totalPriceText.visibility = View.VISIBLE

            holder.itemTextEdit.visibility = View.GONE
            holder.priceTextEdit.visibility = View.GONE
            holder.quantityTextEditContainer.visibility = View.GONE
            holder.totalPriceTextEdit.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        val itemTextEdit: EditText = itemView.findViewById(R.id.itemTextEdit)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val priceTextEdit: EditText = itemView.findViewById(R.id.priceTextEdit)
        val quantityText: TextView = itemView.findViewById(R.id.quantityText)
        val quantityTextEdit: EditText = itemView.findViewById(R.id.quantityTextEdit)
        val quantityTextEditContainer: LinearLayout = itemView.findViewById(R.id.quantityTextEditContainer)
        val totalPriceText: TextView = itemView.findViewById(R.id.totalPriceText)
        val totalPriceTextEdit: EditText = itemView.findViewById(R.id.totalPriceTextEdit)
    }

    fun toggleEditMode() {
        isEditing = !isEditing
        notifyDataSetChanged()
    }
}