package com.example.food_ordering

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SummaryAdapter(private val summaryItems: List<MenuItem>) : RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
    SummaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.summary_item, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val summaryItem = summaryItems[position]
        holder.itemName.text = summaryItem.name
        holder.itemPrice.text = "$${summaryItem.price}"
    }

    override fun getItemCount(): Int {
        return summaryItems.size
    }

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
    }
}
