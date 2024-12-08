package com.example.culinairy.ui.capture_ocr_result

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.Adapter.ProductReceiptAdapter
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentCaptureReceiptResultBinding
import com.example.culinairy.model.Product

class CaptureReceiptResultFragment : Fragment() {

    private var _binding: FragmentCaptureReceiptResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Variables
    private lateinit var productReceiptAdapter: ProductReceiptAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val captureReceiptResultViewModel = ViewModelProvider(this)[CaptureReceiptResultViewModel::class.java]
        _binding = FragmentCaptureReceiptResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Dummy data
        val productList = listOf(
            Product("Item A", 2, 5000, 10000),
            Product("Item B", 1, 3000, 3000),
            Product("Item C", 3, 2000, 6000),
            Product("Item D", 1, 1000, 1000),
            Product("Item E", 2, 4000, 8000),
            Product("Item F", 1, 6000, 6000),
            Product("Item G", 1, 7000, 7000),
            Product("Item H", 1, 8000, 8000),
            Product("Item I", 1, 9000, 9000),
            Product("Item J", 1, 10000, 10000),
        )

        // Initialize adapter
        productReceiptAdapter = ProductReceiptAdapter(productList)
        val recyclerView: RecyclerView = binding.productList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = productReceiptAdapter

        // Set total price
        binding.totalText.text = "Rp${productList.sumOf { it.totalPrice }}"

        // Set up button listeners
        setupButtonListeners(productList)

        return root
    }

    // Set up button listeners
    private fun setupButtonListeners(productList: List<Product>) {
        binding.editButton.setOnClickListener {
            productReceiptAdapter.toggleEditMode()
            binding.saveButton.visibility = View.VISIBLE
        }
        binding.saveButton.setOnClickListener {
            productReceiptAdapter.toggleEditMode()
            binding.saveButton.visibility = View.GONE
            binding.totalText.text = "Rp${productList.sumOf { it.totalPrice }}"

            // Hide keyboard
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = activity?.currentFocus
            if (view != null) {
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        binding.confirmButton.setOnClickListener {
            // Navigate to success fragment
            findNavController().navigate(R.id.action_captureReceiptResultFragment_to_captureReceiptSuccessFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}