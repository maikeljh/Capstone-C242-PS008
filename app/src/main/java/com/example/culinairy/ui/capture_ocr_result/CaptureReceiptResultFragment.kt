package com.example.culinairy.ui.capture_ocr_result

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.Adapter.ProductReceiptAdapter
import com.example.culinairy.MainActivity
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentCaptureReceiptResultBinding
import com.example.culinairy.model.product.Product
import com.example.culinairy.model.transaction.TransactionProduct
import com.example.culinairy.utils.TokenManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CaptureReceiptResultFragment : Fragment() {

    private var _binding: FragmentCaptureReceiptResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Variables
    private lateinit var productReceiptAdapter: ProductReceiptAdapter
    private lateinit var captureReceiptResultViewModel: CaptureReceiptResultViewModel
    private var ocrResult: String? = null
    private var productList: List<TransactionProduct> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        captureReceiptResultViewModel = ViewModelProvider(this)[CaptureReceiptResultViewModel::class.java]
        _binding = FragmentCaptureReceiptResultBinding.inflate(inflater, container, false)
        val root: View = binding.root

        ocrResult = arguments?.getString("ocr_response")
        Log.d("CaptureReceiptResultFragment", "Received OCR Result: $ocrResult")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up observers
        setupObservers()

        // Get User Products
        val mainActivity = requireActivity() as MainActivity
        val token = TokenManager.retrieveToken(mainActivity)
        if (token != null) {
            captureReceiptResultViewModel.getProducts(token)
        }

        // Parse OCR response
        productList = parseOcrResponse(ocrResult)

        // Set total price
        binding.totalText.text = "Rp${productList.sumOf { it.totalPrice }}"

        // Set up button listeners
        setupButtonListeners(productList)
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

    // Set up observers
    private fun setupObservers() {
        captureReceiptResultViewModel.getProductsResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val userProductList = response.data.products

                // Set up adapter
                setupAdapter(productList, userProductList)

            }
        }

        captureReceiptResultViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.darkOverlay1.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.darkOverlay2.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loadingAnimation.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.editButton.isEnabled = !isLoading
            binding.confirmButton.isEnabled = !isLoading
        }

        captureReceiptResultViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Set up adapter
    private fun setupAdapter(productList: List<TransactionProduct>, userProductList: List<Product>) {
        productReceiptAdapter = ProductReceiptAdapter(productList, userProductList) { totalPrice ->
            binding.totalText.text = "Rp$totalPrice"
        }
        val recyclerView: RecyclerView = binding.productList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = productReceiptAdapter
    }

    // Parse OCR response
    private fun parseOcrResponse(ocrResponse: String?): List<TransactionProduct> {
        if (ocrResponse.isNullOrEmpty()) {
            Log.e("ParseOCRResponse", "OCR Response is null or empty")
            return emptyList()
        }

        // Regex
        val itemRegex = Regex("""Item\(product_name=(.*?), quantity=(\d+), price_per_unit=(\d+), total_price=(\d+)(?:, product_id=(.*?))?\)""")

        val products = mutableListOf<TransactionProduct>()

        itemRegex.findAll(ocrResponse).forEach { matchResult ->
            val product =
                TransactionProduct(
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
    private fun setupButtonListeners(productList: List<TransactionProduct>) {
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