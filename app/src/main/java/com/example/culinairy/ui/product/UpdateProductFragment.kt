package com.example.culinairy.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.culinairy.MainActivity
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentCreateProductBinding
import com.example.culinairy.model.product.ProductRequest
import com.example.culinairy.repository.ProductRepository
import com.example.culinairy.utils.TokenManager

class UpdateProductFragment : Fragment() {

    private val updateProductViewModel: UpdateProductViewModel by viewModels {
        UpdateProductViewModelFactory(ProductRepository()) // Pass productService from RetrofitInstance
    }

    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        val mainActivity = requireActivity() as MainActivity
        val token = TokenManager.retrieveToken(mainActivity)
        // Retrieve arguments passed from ProductFragment
        val productId = arguments?.getString("productId")
        val productName = arguments?.getString("productName")
        val productPrice = arguments?.getInt("productPrice")

        // Set data in the UI
        productName?.let {
            binding.namaEt.setText(it)
        }

        productPrice?.let {
            binding.hargaEt.setText(it.toString())
        }

        // Handle updating the product when the update button is clicked
        binding.submitButton.setOnClickListener {
            productId?.let { product_id ->
                val updatedName = binding.namaEt.text.toString()
                val updatedPrice = binding.hargaEt.text.toString().toFloatOrNull()

                if (updatedName.isNotEmpty() && updatedPrice != null) {
                    val productRequest = ProductRequest(
                        product_name = updatedName,
                        price = updatedPrice.toInt()
                    )
                    // Call ViewModel to update the product
                    if (token != null) {
                        updateProductViewModel.updateProduct(token, product_id, productRequest)
                    }
                    Toast.makeText(requireContext(), "Product updated!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_product) // Navigate back to product list
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

