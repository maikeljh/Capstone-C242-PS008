package com.example.culinairy.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentHomeBinding
import com.example.culinairy.ListTransactionActivity
import com.example.culinairy.repository.TransactionRepository
import com.example.culinairy.utils.TokenManager
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Bind the ViewModel and lifecycle owner
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observe error messages
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch user data
        val token = TokenManager.retrieveToken(requireContext())
        token?.let {
            viewModel.fetchUser(it)
            viewModel.loadTransactions(it)
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingAnimation.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.darkOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Setup recent transactions
        setupRecentTransactions()

        return binding.root
    }

    private fun setupRecentTransactions() {
        viewModel.transactionsResult.observe(viewLifecycleOwner) { result ->
            binding.transactionContainer.removeAllViews()

            val titleTextView = TextView(context).apply {
                id = View.generateViewId()
                text = getString(R.string.recent_transactions_title)
                textSize = 16f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = android.view.Gravity.START
                    bottomMargin = 8
                }
            }
            binding.transactionContainer.addView(titleTextView)

            when (result) {
                is TransactionRepository.Result.Success -> {
                    val transactions = result.data.data.transactions

                    val latestTransactions = transactions
                        .sortedByDescending { it.timestamp }
                        .take(3)

                    latestTransactions.forEach { transaction ->
                        val transactionView = LayoutInflater.from(context).inflate(
                            R.layout.item_transaction,
                            binding.transactionContainer,
                            false
                        )

                        transactionView.findViewById<TextView>(R.id.transactionTitle).text =
                            "Payment from #${transaction.transaction_id}"
                        transactionView.findViewById<TextView>(R.id.transactionDate).text =
                            formatTimestamp(transaction.timestamp)
                        transactionView.findViewById<TextView>(R.id.transactionAmount).text =
                            "+Rp${NumberFormat.getNumberInstance(Locale.US).format(transaction.total_price)}"

                        binding.transactionContainer.addView(transactionView)
                    }
                }
                is TransactionRepository.Result.Error -> {
                    Toast.makeText(
                        context,
                        "Error loading transactions: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TransactionRepository.Result.Loading -> {
                    Toast.makeText(context, "Loading transactions...", Toast.LENGTH_SHORT).show()
                }
            }

            val seeAllTextView = TextView(context).apply {
                id = View.generateViewId()
                text = getString(R.string.see_all_transactions)
                textSize = 16f
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = android.view.Gravity.CENTER
                    topMargin = 64
                }
            }

            seeAllTextView.setOnClickListener {
                val intent = Intent(requireContext(), ListTransactionActivity::class.java)
                startActivity(intent)
            }

            binding.transactionContainer.addView(seeAllTextView)
        }
    }

    private fun formatTimestamp(timestamp: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val date = inputFormat.parse(timestamp.trim())
            if (date != null) {
                outputFormat.format(date)
            } else {
                "Invalid timestamp"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            timestamp
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
