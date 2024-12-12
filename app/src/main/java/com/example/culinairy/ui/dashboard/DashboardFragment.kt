package com.example.culinairy.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.culinairy.MainActivity
import com.example.culinairy.databinding.FragmentDashboardBinding
import com.example.culinairy.repository.TransactionRepository
import com.example.culinairy.utils.TokenManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.NumberFormat
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = requireActivity() as MainActivity

        val token = TokenManager.retrieveToken(mainActivity)

        // Setup charts
        setupLineChart(binding.lineChart)

        // Initialize DashboardViewModel
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Observe transactions
        dashboardViewModel.transactionsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransactionRepository.Result.Loading -> {
                    android.util.Log.d("DashboardFragment", "Loading transactions...")
                    binding.omsetValue.text = "Calculating..."
                    binding.orderValue.text = "Calculating..."
                }
                is TransactionRepository.Result.Success -> {
                    val transactionAllResponse = result.data
                    val transactions = transactionAllResponse.data.transactions
                    val meta = transactionAllResponse.data.meta

                    val totalOmset = transactions.sumOf { it.total_price }
                    val formattedOmset = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
                        maximumFractionDigits = 0
                    }.format(totalOmset)

                    binding.omsetValue.text = "$formattedOmset"
                    binding.orderValue.text = meta.total.toString()
                }
                is TransactionRepository.Result.Error -> {
                    android.util.Log.e("DashboardFragment", "Error loading transactions: ${result.message}")
                    binding.omsetValue.text = "Null"
                    binding.orderValue.text = "Null"
                }
            }
        }

        // Observe top products
        dashboardViewModel.topProductsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransactionRepository.Result.Loading -> {
                    android.util.Log.d("DashboardFragment", "Loading top 5 products...")
                }
                is TransactionRepository.Result.Success -> {
                    // Map repository TopProduct to DashboardViewModel TopProduct
                    val topProducts = result.data.data.map { repoProduct ->
                        DashboardViewModel.TopProduct(
                            productName = repoProduct.product_name,
                            totalQuantity = repoProduct.total_quantity,
                        )
                    }
                    setupBarChart(binding.barChart, topProducts)
                }
                is TransactionRepository.Result.Error -> {
                    android.util.Log.e("DashboardFragment", "Error loading top 5 products: ${result.message}")
                }
            }
        }

        // Trigger data load
        if (token != null) {
            dashboardViewModel.loadTransactions(token)
            dashboardViewModel.loadTopProducts(token)
        }

        return root
    }

    private fun setupLineChart(lineChart: LineChart) {
        val entries = listOf(
            Entry(0f, 1f),
            Entry(1f, 10f),
            Entry(2f, 50f),
            Entry(3f, 30f),
            Entry(4f, 10f),
            Entry(5f, 200f)
        )

        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.color = Color.parseColor("#3E2723")
        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 5f
        lineDataSet.setCircleColor(Color.parseColor("#3E2723"))
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.valueTextSize = 0f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "TODAY"))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = Color.parseColor("#6C757D")
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.yOffset = 10f

        val leftAxis = lineChart.axisLeft
        leftAxis.textSize = 12f
        leftAxis.textColor = Color.parseColor("#6C757D")
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = Color.parseColor("#F2E9E4")
        leftAxis.gridLineWidth = 1f
        leftAxis.spaceTop = 15f

        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.setExtraOffsets(10f, 10f, 10f, 10f)
        lineChart.animateX(1000)
        lineChart.animateY(1000)
        lineChart.invalidate()
    }

    private fun setupBarChart(barChart: BarChart, topProducts: List<DashboardViewModel.TopProduct>) {
        val entries = topProducts.mapIndexed { index, product ->
            BarEntry(index.toFloat(), product.totalQuantity.toFloat())
        }
        val labels = topProducts.map { it.productName }

        val barDataSet = BarDataSet(entries, "Top 5 Products")
        barDataSet.colors = listOf(
            Color.parseColor("#F5CBA7"),
            Color.parseColor("#5DADE2"),
            Color.parseColor("#34495E"),
            Color.parseColor("#A93226"),
            Color.parseColor("#7DCEA0")
        )
        barDataSet.valueTextSize = 12f
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)
        barData.barWidth = 0.3f
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.textColor = Color.parseColor("#6C757D")
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.labelRotationAngle = 0f

        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setExtraOffsets(10f, 10f, 10f, 30f)
        barChart.animateY(1000)
        barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
