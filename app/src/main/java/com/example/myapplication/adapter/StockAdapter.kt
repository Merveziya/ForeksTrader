package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemStockBinding
import com.example.myapplication.model.Fields
import com.example.myapplication.model.StockListDetail

class StockAdapter(private val stockList: List<StockListDetail>, private val stockRequest: List<Fields> ) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stockList.size    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val currentStock = stockList[position]
        holder.binding.apply {
            name.text = currentStock.cod

            val field = stockRequest.getOrNull(position)
            lastUpdateTime.text = field?.clo ?: "null"
            leftHeaderValue.text = field?.las ?: "null"
            rightHeaderValue.text = field?.pdd ?: "null"
        }
    }
}
