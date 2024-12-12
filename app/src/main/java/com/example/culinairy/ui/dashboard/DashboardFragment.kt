package com.example.culinairy.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    // Flags to track loading states
    private var isTransactionsLoading = false
    private var isTopProductsLoading = false
    private var isOmsetLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val mainActivity = requireActivity() as MainActivity

        val token = TokenManager.retrieveToken(mainActivity)

        // Initialize DashboardViewModel
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Observe transactions
        dashboardViewModel.transactionsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransactionRepository.Result.Loading -> {
                    isTransactionsLoading = true
                    updateLoadingState()
                }
                is TransactionRepository.Result.Success -> {
                    isTransactionsLoading = false
                    val transactionAllResponse = result.data
                    val transactions = transactionAllResponse.data.transactions
                    val meta = transactionAllResponse.data.meta

                    val totalOmset = transactions.sumOf { it.total_price }
                    val formattedOmset = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
                        maximumFractionDigits = 0
                    }.format(totalOmset)

                    binding.omsetValue.text = "$formattedOmset"
                    binding.orderValue.text = meta.total.toString()
                    updateLoadingState()
                }
                is TransactionRepository.Result.Error -> {
                    isTransactionsLoading = false
                    android.util.Log.e("DashboardFragment", "Error loading transactions: ${result.message}")
                    binding.omsetValue.text = "Null"
                    binding.orderValue.text = "Null"
                    updateLoadingState()
                }
            }
        }

        // Observe top products
        dashboardViewModel.topProductsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransactionRepository.Result.Loading -> {
                    isTopProductsLoading = true
                    updateLoadingState()
                }
                is TransactionRepository.Result.Success -> {
                    isTopProductsLoading = false
                    val topProducts = result.data.data.map { repoProduct ->
                        DashboardViewModel.TopProduct(
                            productName = repoProduct.product_name,
                            totalQuantity = repoProduct.total_quantity,
                        )
                    }
                    setupBarChart(binding.barChart, topProducts)
                    updateLoadingState()
                }
                is TransactionRepository.Result.Error -> {
                    isTopProductsLoading = false
                    android.util.Log.e("DashboardFragment", "Error loading top 5 products: ${result.message}")
                    updateLoadingState()
                }
            }
        }

        // Observe omset
        dashboardViewModel.omsetResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransactionRepository.Result.Loading -> {
                    isOmsetLoading = true
                    updateLoadingState()
                }
                is TransactionRepository.Result.Success -> {
                    isOmsetLoading = false
                    val omsetResponse = result.data.data
                    val currentData = omsetResponse.currentData
                    val predictions = omsetResponse.predictions

                    val last12CurrentData = currentData.takeLast(12)

                    val totalOmset = last12CurrentData.sumOf { it.price }
                    val formattedOmset = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
                        maximumFractionDigits = 0
                    }.format(totalOmset)
                    binding.omsetValue.text = formattedOmset

                    val chartEntries = mutableListOf<Entry>()

                    last12CurrentData.forEachIndexed { index, dataPoint ->
                        chartEntries.add(Entry(index.toFloat(), dataPoint.price.toFloat()))
                    }

                    predictions.forEachIndexed { index, prediction ->
                        chartEntries.add(Entry((last12CurrentData.size + index).toFloat(), prediction.price.toFloat()))
                    }

                    setupLineChart(binding.lineChart, chartEntries, last12CurrentData.size, predictions.size)

                    updateLoadingState()
                }
                is TransactionRepository.Result.Error -> {
                    isOmsetLoading = false
                    android.util.Log.e("DashboardFragment", "Error loading omset data: ${result.message}")
                    binding.omsetValue.text = "Error"
                    updateLoadingState()
                }
            }
        }

        if (token != null) {
            dashboardViewModel.loadTransactions(token)
            dashboardViewModel.loadTopProducts(token)
            dashboardViewModel.loadOmset(token)
        }

        return root
    }

    private fun setupLineChart(lineChart: LineChart, chartEntries: List<Entry>, currentDataSize: Int, predictionSize: Int) {
        val currentDataEntries = chartEntries.take(currentDataSize).toMutableList()
        val predictionEntries = chartEntries.takeLast(predictionSize).toMutableList()

        if (currentDataEntries.isNotEmpty() && predictionEntries.isNotEmpty()) {
            val transitionEntry = Entry(
                currentDataEntries.last().x,
                currentDataEntries.last().y,
            )
            predictionEntries.add(0, transitionEntry)
        }

        val currentDataDataSet = LineDataSet(currentDataEntries, "Current Data").apply {
            color = Color.BLACK
            setCircleColor(Color.BLACK)
            setDrawCircleHole(false)
            lineWidth = 2.5f
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = Color.LTGRAY
        }

        val predictionDataSet = LineDataSet(predictionEntries, "Predictions").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            setDrawCircleHole(false)
            lineWidth = 2.5f
            valueTextSize = 10f
            setDrawFilled(true)
            fillColor = Color.CYAN
        }

        val lineData = LineData(currentDataDataSet, predictionDataSet)

        lineChart.data = lineData

        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            valueFormatter = IndexAxisValueFormatter(generateDateLabels(currentDataSize + predictionSize))
            textSize = 12f
            textColor = Color.DKGRAY
            setDrawGridLines(true)
        }

        lineChart.axisLeft.apply {
            textColor = Color.DKGRAY
            setDrawGridLines(true)
        }

        lineChart.axisRight.isEnabled = false

        lineChart.apply {
            description.text = "Sales Performance"
            description.textColor = Color.DKGRAY
            description.textSize = 12f
            legend.isEnabled = true // Show legend
            setExtraOffsets(10f, 10f, 10f, 10f)
            setTouchEnabled(true)
            setPinchZoom(true)
            setScaleEnabled(true)
        }

        lineChart.animateX(1000)
        lineChart.invalidate()
    }

    private fun generateDateLabels(totalPoints: Int): List<String> {
        val dateLabels = mutableListOf<String>()
        val calendar = java.util.Calendar.getInstance()

        for (i in 0 until totalPoints) {
            val dateFormat = java.text.SimpleDateFormat("dd MMM", Locale.getDefault())
            dateLabels.add(dateFormat.format(calendar.time))
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }

        return dateLabels
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

    private fun updateLoadingState() {
        val isLoading = isTransactionsLoading || isTopProductsLoading || isOmsetLoading
        binding.loadingAnimation.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.darkOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
