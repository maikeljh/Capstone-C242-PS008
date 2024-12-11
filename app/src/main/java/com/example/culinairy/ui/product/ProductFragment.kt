package com.example.culinairy.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.culinairy.Adapter.ProductAdapter
import com.example.culinairy.MainActivity
import com.example.culinairy.databinding.FragmentListProductBinding
import com.example.culinairy.model.product.Product
import com.example.culinairy.repository.ProductRepository
import com.example.culinairy.services.RetrofitInstance
import com.example.culinairy.utils.TokenManager

class ProductFragment : Fragment() {

    private var _binding: FragmentListProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    // Use the factory to create the ViewModel
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

        return binding.root
    }

    private fun showEditProductDialog(product: Product) {
        Toast.makeText(requireContext(), "Edit ${product.product_name}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




