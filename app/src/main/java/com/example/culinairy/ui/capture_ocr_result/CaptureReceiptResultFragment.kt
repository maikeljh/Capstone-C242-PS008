package com.example.culinairy.ui.capture_ocr_result

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.Adapter.ProductReceiptAdapter
import com.example.culinairy.databinding.FragmentCaptureReceiptResultBinding
import com.example.culinairy.model.Product

class CaptureReceiptResultFragment : Fragment() {

    private var _binding: FragmentCaptureReceiptResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var productReceiptAdapter: ProductReceiptAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val captureReceiptResultViewModel = ViewModelProvider(this)[CaptureReceiptResultViewModel::class.java]

        _binding = FragmentCaptureReceiptResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val productList = listOf(
            Product("Item A", 2, 5000.0, 10000.0),
            Product("Item B", 1, 3000.0, 3000.0),
            Product("Item C", 3, 2000.0, 6000.0)
        )

        productReceiptAdapter = ProductReceiptAdapter(productList)

        val recyclerView: RecyclerView = binding.productList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = productReceiptAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}