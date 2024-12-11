package com.example.culinairy.ui.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.culinairy.MainActivity
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentEditProfileBinding
import com.example.culinairy.model.auth.UpdateUserRequestBody
import com.example.culinairy.utils.TokenManager

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingAnimation.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.darkOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // fetch user data
        fetchUserData()

        // update user
        binding.saveButton.setOnClickListener {
            val name = binding.namaEt.text.toString()
            val address = binding.posisiEt.text.toString()
            val token = TokenManager.retrieveToken(requireActivity() as MainActivity)

            if (token != null && name.isNotBlank() && address.isNotBlank()) {
                viewModel.updateProfile(token, UpdateUserRequestBody(name, address))
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.navigation_profile)
            } else {
                Toast.makeText(requireContext(), "Name and address should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }
    override fun onResume() {
        super.onResume()
        fetchUserData()
    }

    private fun fetchUserData() {
        val mainActivity = requireActivity() as MainActivity
        val token = TokenManager.retrieveToken(mainActivity)
        if (token != null) {
            viewModel.fetchUser(token)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}