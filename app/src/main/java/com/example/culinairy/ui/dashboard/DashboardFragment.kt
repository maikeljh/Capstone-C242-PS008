package com.example.culinairy.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.culinairy.Adapter.LabelAdapter
import com.example.culinairy.R
import com.example.culinairy.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
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
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Prepare the dropdown list
        val items = listOf("Hari ini", "Minggu ini", "Bulan ini")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        binding.autoCompleteTextView.setAdapter(adapter)

        // Optional: Handle dropdown item selection
        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = items[position]
            // Use the selectedItem as needed
        }

        // Setup charts
        setupLineChart(binding.lineChart)
        setupPieChart(binding.pieChart)

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

    private fun setupPieChart(pieChart: PieChart) {
        // Example Data
        val entries = listOf(
            PieEntry(30f, "Ikan Gurame"),
            PieEntry(30f, "Ikan Nila"),
            PieEntry(10f, "Kepiting Besar"),
            PieEntry(30f, "Cumi Besar")
        )

        val pieDataSet = PieDataSet(entries, "")
        val colors = listOf(
            Color.parseColor("#F5CBA7"), // Ikan Gurame
            Color.parseColor("#5DADE2"), // Ikan Nila
            Color.parseColor("#34495E"), // Kepiting Besar
            Color.parseColor("#A93226")  // Cumi Besar
        )
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 0f // Hide percentage labels on the chart

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData

        // Customize the chart
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 65f // Thin donut ring
        pieChart.transparentCircleRadius = 70f
        pieChart.setDrawEntryLabels(false) // Hide labels on the chart
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.animateY(1000)

        // Dynamically generate the labels
        generateLabels(entries, colors)
    }

    private fun generateLabels(entries: List<PieEntry>, colors: List<Int>) {
        val adapter = LabelAdapter(entries, colors)
        binding.labelRecyclerView.adapter = adapter
        binding.labelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
