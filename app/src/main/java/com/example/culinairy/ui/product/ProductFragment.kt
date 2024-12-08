package com.example.culinairy.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.Adapter.ProductReceiptAdapter
import com.example.culinairy.R
import com.example.culinairy.model.Product
import com.example.culinairy.databinding.FragmentListProductBinding

class ProductFragment : Fragment() {

    private var _binding: FragmentListProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductReceiptAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize RecyclerView
        recyclerView = binding.root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Dummy data for the products
        val productList = listOf(
            Product("Item 1", 1, 10000.0, 10000.0),
            Product("Item 2", 2, 20000.0, 40000.0),
            Product("Item 3", 3, 15000.0, 45000.0)
        )

        // Initialize and set the adapter
        productAdapter = ProductReceiptAdapter(productList)
        recyclerView.adapter = productAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

