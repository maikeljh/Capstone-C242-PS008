package com.example.culinairy.ui.product

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.culinairy.R
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.databinding.FragmentCreateProductBinding
import com.example.culinairy.repository.ProductOnlyRepository
import com.example.culinairy.services.ProductService
import com.example.culinairy.services.RetrofitInstance

class CreateProductFragment : Fragment(R.layout.fragment_create_product) {

    private val createProductViewModel: CreateProductViewModel by viewModels {
        CreateProductViewModelFactory(ProductOnlyRepository(RetrofitInstance.productService)) // Pass productService from RetrofitInstance
    }

    private lateinit var binding: FragmentCreateProductBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateProductBinding.bind(view)

        createProductViewModel.createProductResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                if (it.status == "success") {
                    // Product created successfully, show a success message
                    Toast.makeText(requireContext(), "Product created successfully!", Toast.LENGTH_SHORT).show()

                    // Clear the input fields
                    binding.namaEt.text?.clear()
                    binding.hargaEt.text?.clear()

                } else {
                    // Handle failure case
                    Toast.makeText(requireContext(), "Failed to create product: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        createProductViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })

        // Set up form submission
        binding.submitButton.setOnClickListener {
            val userId = "someUserId" // TODO
            val productName = binding.namaEt.text.toString()
            val price = binding.hargaEt.text.toString().toIntOrNull()

            if (productName.isNotEmpty() && price != null) {
                val productRequest = ProductRequest(
                    user_id = userId,
                    product_name = productName,
                    price = price
                )
                createProductViewModel.createProduct(productRequest)
            } else {
                if (productName.isEmpty()) {
                    Toast.makeText(requireContext(), "Product name is required", Toast.LENGTH_SHORT).show()

                if (price == null) {
                    Toast.makeText(requireContext(), "Valid price is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

