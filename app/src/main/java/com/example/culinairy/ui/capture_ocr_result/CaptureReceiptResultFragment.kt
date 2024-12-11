package com.example.culinairy.ui.capture_ocr_result

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONObject

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

        val ocrResult = arguments?.getString("ocr_response")
        Log.d("CaptureReceiptResultFragment", "Received OCR Result: $ocrResult")

        // Parse OCR response
        val productList = parseOcrResponse(ocrResult)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        // Remove navbar
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.GONE
        val fab: FloatingActionButton = requireActivity().findViewById(R.id.fab)
        fab.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navView.visibility = View.VISIBLE
        val fab: FloatingActionButton = requireActivity().findViewById(com.example.culinairy.R.id.fab)
        fab.visibility = View.VISIBLE
    }

    // Parse OCR response
    private fun parseOcrResponse(ocrResponse: String?): List<Product> {
        if (ocrResponse.isNullOrEmpty()) {
            Log.e("ParseOCRResponse", "OCR Response is null or empty")
            return emptyList()
        }

        // Regex
        val itemRegex = Regex("""Item\(product_name=(.*?), quantity=(\d+), price_per_unit=(\d+), total_price=(\d+)(?:, product_id=(.*?))?\)""")

        val products = mutableListOf<Product>()

        itemRegex.findAll(ocrResponse).forEach { matchResult ->
            val product = Product(
                matchResult.groupValues[1],                    // productName
                matchResult.groupValues[2].toInt(),            // quantity
                matchResult.groupValues[3].toInt(),            // pricePerUnit
                matchResult.groupValues[4].toInt(),            // totalPrice
            )
            products.add(product)
        }

        return products
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
}