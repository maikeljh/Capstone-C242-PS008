package com.example.culinairy.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.culinairy.Adapter.ProductAdapter
import com.example.culinairy.MainActivity
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentListProductBinding
import com.example.culinairy.model.product.Product
import com.example.culinairy.utils.TokenManager

class ProductFragment : Fragment() {

    private var _binding: FragmentListProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private val productViewModel: ProductViewModel by viewModels { ProductViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListProductBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        productAdapter = ProductAdapter(emptyList()) { product ->
            showEditProductDialog(product)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = productAdapter

        // Fetch token
        val mainActivity = requireActivity() as MainActivity
        val token = TokenManager.retrieveToken(mainActivity)

        // Observe LiveData
        productViewModel.products.observe(viewLifecycleOwner) { response ->
            response?.let {
                productAdapter.updateData(it.data.products)
            }
        }

        productViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch products
        token?.let {
            productViewModel.fetchProducts(it)
        }

        // Set up search
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter(newText ?: "")
                return true
            }
        })

        // Navigate to CreateProductFragment
        binding.addProductButton.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_createProductFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // Fetch products
        // Fetch token
        val mainActivity = requireActivity() as MainActivity
        val token = TokenManager.retrieveToken(mainActivity)
        token?.let {
            productViewModel.fetchProducts(it)
        }
    }

    private fun showEditProductDialog(product: Product) {
        val bundle = Bundle().apply {
            putString("productId", product.product_id)
            putString("productName", product.product_name)
            putFloat("productPrice", product.price.toFloat())
        }
        findNavController().navigate(R.id.navigation_update_product, bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





