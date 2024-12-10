package com.example.culinairy.ui.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.culinairy.MainActivity
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentEditProfileBinding
import com.example.culinairy.databinding.FragmentProfileBinding
import com.example.culinairy.utils.LogoutManager

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

        binding.saveButton.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            LogoutManager.logout(mainActivity)
            findNavController().navigate(R.id.navigation_profile)
        }

        return root
    }
}