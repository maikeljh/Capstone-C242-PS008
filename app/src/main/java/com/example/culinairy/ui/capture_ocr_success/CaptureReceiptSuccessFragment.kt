package com.example.culinairy.ui.capture_ocr_success

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.culinairy.MainActivity
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentCaptureReceiptSuccessBinding
import com.example.culinairy.databinding.FragmentProfileBinding
import com.example.culinairy.ui.capture_ocr_result.CaptureReceiptResultViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            (requireActivity() as? MainActivity)?.updateBottomNavSelection(R.id.navigation_home)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Remove back button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
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
}