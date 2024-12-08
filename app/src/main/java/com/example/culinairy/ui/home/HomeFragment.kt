package com.example.culinairy.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentHomeBinding
import com.example.culinairy.ListTransactionActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.greetingText.text = "Halo Budi"
        binding.omsetValue.text = "Rp5.560.000"
        binding.orderValue.text = "375"

        setupRecentTransactions()

        return binding.root
    }

    private fun setupRecentTransactions() {
        val transactionList = listOf(
            Triple("Payment from #1132", "Hari ini, 24 November 2024 - 20:13", "+Rp560.000"),
            Triple("Payment from #1133", "Hari ini, 24 November 2024 - 19:32", "+Rp60.000"),
            Triple("Payment from #1134", "Hari ini, 24 November 2024 - 19:12", "+Rp1.560.000")
        )

        binding.transactionContainer.removeAllViews()

        val titleTextView = TextView(context).apply {
            id = View.generateViewId()
            text = "Daftar Transaksi Terbaru"
            textSize = 16f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setTextColor(resources.getColor(R.color.black, null))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.START
                bottomMargin = 8
            }
        }

        binding.transactionContainer.addView(titleTextView)

        transactionList.forEach { (title, date, amount) ->
            val transactionView = LayoutInflater.from(context).inflate(
                R.layout.item_transaction,
                binding.transactionContainer,
                false
            )

            transactionView.findViewById<TextView>(R.id.transactionTitle).text = title
            transactionView.findViewById<TextView>(R.id.transactionDate).text = date
            transactionView.findViewById<TextView>(R.id.transactionAmount).text = amount

            binding.transactionContainer.addView(transactionView)
        }

        val seeAllTextView = TextView(context).apply {
            id = View.generateViewId()
            text = "Lihat Semua Transaksi"
            textSize = 16f
            setTextColor(resources.getColor(R.color.black, null))
            setTypeface(typeface, android.graphics.Typeface.BOLD)

            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
                (this as? ViewGroup.MarginLayoutParams)?.topMargin = 64
            }
        }

        seeAllTextView.setOnClickListener {
            val intent = Intent(requireContext(), ListTransactionActivity::class.java)
            startActivity(intent)
        }

        binding.transactionContainer.addView(seeAllTextView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
