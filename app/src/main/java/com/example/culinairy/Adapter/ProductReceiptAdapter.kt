package com.example.culinairy.Adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R
import com.example.culinairy.model.product.Product
import com.example.culinairy.model.transaction.TransactionProduct

class ProductReceiptAdapter(
    private val productList: List<TransactionProduct>,
    private val userProductList: List<Product>,
    private val totalPriceCallback: (Int) -> Unit
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

        holder.priceText.text = "Rp${product.price}"

        holder.quantityText.text = "${product.quantity}x"
        holder.quantityTextEdit.setText(product.quantity.toString())

        holder.totalPriceText.text = "Rp${product.totalPrice}"

        val categories = userProductList.map { it.product_name }.sorted()

        // Set up spinner
        val adapter = ArrayAdapter(holder.itemView.context, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        holder.itemEdit.adapter = adapter

        // Set spinner selection based on product name
        val categoryPosition = categories.indexOf(product.name)
        holder.itemEdit.setSelection(if (categoryPosition >= 0) categoryPosition else 0)

        holder.itemEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                product.name = categories[position]

                val selectedProduct = userProductList.find { it.product_name == product.name }
                if (selectedProduct != null) {
                    product.id = selectedProduct.product_id

                    product.price = selectedProduct.price
                    holder.priceText.text = "Rp${product.price}"

                    product.totalPrice = product.price * product.quantity
                    holder.totalPriceText.text = "Rp${product.totalPrice}"

                    totalPriceCallback(getTotalPrice())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        holder.quantityTextEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                product.quantity = s.toString().toIntOrNull() ?: 0

                product.totalPrice = product.price * product.quantity
                holder.totalPriceText.text = "Rp${product.totalPrice}"

                totalPriceCallback(getTotalPrice())
            }
        })

        if (isEditing) {
            holder.itemEdit.visibility = View.VISIBLE
            holder.quantityTextEditContainer.visibility = View.VISIBLE

            holder.itemText.setTextColor(holder.itemView.context.getColor(R.color.white))
            holder.quantityText.visibility = View.GONE
        } else {
            holder.itemText.setTextColor(holder.itemView.context.getColor(R.color.black))
            holder.quantityText.visibility = View.VISIBLE

            holder.itemEdit.visibility = View.GONE
            holder.quantityTextEditContainer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = productList.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.itemText)
        val itemEdit: Spinner = itemView.findViewById(R.id.itemEdit)
        val priceText: TextView = itemView.findViewById(R.id.priceText)
        val quantityText: TextView = itemView.findViewById(R.id.quantityText)
        val quantityTextEdit: EditText = itemView.findViewById(R.id.quantityTextEdit)
        val quantityTextEditContainer: LinearLayout = itemView.findViewById(R.id.quantityTextEditContainer)
        val totalPriceText: TextView = itemView.findViewById(R.id.totalPriceText)
    }

    fun toggleEditMode() {
        isEditing = !isEditing
        notifyDataSetChanged()
    }

    private fun getTotalPrice(): Int {
        return productList.sumOf { it.totalPrice }
    }
}