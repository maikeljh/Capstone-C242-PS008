package com.example.culinairy.Adapter

import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.culinairy.R
import com.github.mikephil.charting.data.PieEntry

class LabelAdapter(
    private val entries: List<PieEntry>,
    private val colors: List<Int>
) : RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    inner class LabelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorIndicator: View = view.findViewById(R.id.colorIndicator)
        val labelText: TextView = view.findViewById(R.id.labelText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_label, parent, false)
        return LabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        // Create a circular drawable
        val circleDrawable = ShapeDrawable(OvalShape())
        circleDrawable.intrinsicWidth = 16 // Set width for the circle
        circleDrawable.intrinsicHeight = 16 // Set height for the circle
        circleDrawable.paint.color = colors[position] // Set the color dynamically
        circleDrawable.paint.style = Paint.Style.FILL

        // Apply the circle drawable as the background
        holder.colorIndicator.background = circleDrawable

        // Set the label text
        holder.labelText.text = "${entries[position].label} ${entries[position].value.toInt()} (${entries[position].value.toInt()}%)"
    }

    override fun getItemCount(): Int = entries.size
}