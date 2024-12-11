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

        val items = listOf("Hari ini", "Minggu ini", "Bulan ini")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        val token = TokenManager.retrieveToken(mainActivity)
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = items[position]
            // Use the selectedItem as needed
        }

        // Setup charts
        setupLineChart(binding.lineChart)
        setupBarChart(binding.barChart)

        // Initialize DashboardViewModel without factory
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Observe data
        dashboardViewModel.transactionsResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TransactionRepository.Result.Loading -> {
                    android.util.Log.d("DashboardFragment", "Loading transactions...")
                }
                is TransactionRepository.Result.Success -> {
                    android.util.Log.d("DashboardFragment", "Transactions loaded successfully: ${result.data}")
                }
                is TransactionRepository.Result.Error -> {
                    android.util.Log.e("DashboardFragment", "Error loading transactions: ${result.message}")
                }
            }
        }

        // Trigger data load
        if (token != null) {
            dashboardViewModel.loadTransactions(token)
        }
        return root
    }

    private fun setupLineChart(lineChart: LineChart) {
        // Example data
        val entries = listOf(
            Entry(0f, 1f),
            Entry(1f, 10f),
            Entry(2f, 50f),
            Entry(3f, 30f),
            Entry(4f, 10f),
            Entry(5f, 200f)
        )

        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.color = Color.parseColor("#3E2723") // Line color
        lineDataSet.lineWidth = 2f // Line thickness
        lineDataSet.circleRadius = 5f // Circle radius
        lineDataSet.setCircleColor(Color.parseColor("#3E2723")) // Circle color
        lineDataSet.setDrawCircleHole(false) // Solid circles
        lineDataSet.valueTextSize = 0f // Hide value labels

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // Customize X-Axis
        val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "TODAY")
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(days)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = Color.parseColor("#6C757D")
        xAxis.setDrawAxisLine(false) // Remove x-axis line
        xAxis.setDrawGridLines(false) // Remove grid lines
        xAxis.setLabelCount(days.size, false) // Show all labels without skipping
        xAxis.granularity = 1f // Ensure labels are spaced evenly
        xAxis.isGranularityEnabled = true
        xAxis.yOffset = 10f // Add space for labels at the bottom

        // Customize Y-Axis
        val leftAxis = lineChart.axisLeft
        leftAxis.textSize = 12f
        leftAxis.textColor = Color.parseColor("#6C757D")
        leftAxis.axisMinimum = 0f // Start from 0
        leftAxis.setDrawAxisLine(false) // Remove left axis line
        leftAxis.setDrawGridLines(true) // Keep left grid lines
        leftAxis.gridColor = Color.parseColor("#F2E9E4") // Light grid line color
        leftAxis.gridLineWidth = 1f // Thin grid lines
        leftAxis.spaceTop = 15f // Add space above the highest value

        // Disable right axis
        lineChart.axisRight.isEnabled = false

        // Remove legend and description
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false

        // Add padding around the chart
        lineChart.setExtraOffsets(10f, 10f, 10f, 10f)

        // Animation
        lineChart.animateX(1000)
        lineChart.animateY(1000)

        // Refresh chart
        lineChart.invalidate()
    }

    private fun setupBarChart(barChart: BarChart) {
        val entries = listOf(
            BarEntry(0f, 120f),
            BarEntry(1f, 95f),
            BarEntry(2f, 85f),
            BarEntry(3f, 80f),
            BarEntry(4f, 75f)
        )

        val labels = listOf("Ikan Gurame", "Ikan Nila", "Kepiting Besar", "Cumi Besar", "Ikan Mas")
        val colors = listOf(
            Color.parseColor("#F5CBA7"),
            Color.parseColor("#5DADE2"),
            Color.parseColor("#34495E"),
            Color.parseColor("#A93226"),
            Color.parseColor("#7DCEA0")
        )

        val barDataSet = BarDataSet(entries, "Top 5 Products")
        barDataSet.colors = colors
        barDataSet.valueTextSize = 12f
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)
        barData.barWidth = 0.3f // Reduce bar width for better spacing
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f // Smaller text size
        xAxis.textColor = Color.parseColor("#6C757D")
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.labelRotationAngle = 45f // Rotate labels to avoid overlap

        barChart.axisLeft.axisMinimum = 0f
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setExtraOffsets(10f, 10f, 10f, 30f) // Add padding to bottom
        barChart.animateY(1000)

        barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
