package com.example.culinairy.ui.capture_ocr_success

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentCaptureReceiptSuccessBinding
import com.example.culinairy.databinding.FragmentProfileBinding
import com.example.culinairy.ui.capture_ocr.ProfileViewModel
import com.example.culinairy.ui.capture_ocr_result.CaptureReceiptResultViewModel

class CaptureReceiptSuccessFragment : Fragment() {

    private var _binding: FragmentCaptureReceiptSuccessBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val captureReceiptSuccessViewModel = ViewModelProvider(this)[CaptureReceiptSuccessViewModel::class.java]
        _binding = FragmentCaptureReceiptSuccessBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backToHomeButton.setOnClickListener {
            // Navigate to home
            findNavController().navigate(R.id.action_captureReceiptSuccessFragment_to_navigation_home)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}