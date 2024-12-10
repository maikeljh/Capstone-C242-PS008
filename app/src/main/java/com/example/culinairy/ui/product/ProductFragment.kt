package com.example.culinairy.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
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
            Product("Item 1", 1, 10000, 10000),
            Product("Item 2", 2, 20000, 40000),
            Product("Item 3", 3, 15000, 45000)
        )

        // Initialize and set the adapter
        productAdapter = ProductReceiptAdapter(productList)
        recyclerView.adapter = productAdapter

        // Set up the "Add Product" button
        binding.addProductButton.setOnClickListener {
            // Navigate to the Create Product Fragment
            findNavController().navigate(R.id.action_productFragment_to_createProductFragment)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

