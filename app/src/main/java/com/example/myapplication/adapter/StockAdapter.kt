package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemStockBinding
import com.example.myapplication.model.Fields
import com.example.myapplication.model.StockListDetail
import com.example.myapplication.viewmodel.MainViewModel

class StockAdapter(private val stockList: List<StockListDetail>, private val stockRequest: List<Fields> , private val viewModel: MainViewModel) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val currentStock = stockList[position]
        val field = stockRequest.getOrNull(position)

        holder.binding.apply {
            name.text = currentStock.cod
            lastUpdateTime.text = field?.clo ?: "null"
            leftHeaderValue.text = field?.las ?: "null"

            val pddString = field?.pdd ?: "0"
            val pddValue = pddString.replace(",", ".").toDoubleOrNull() ?: 0.0
            rightHeaderValue.text = pddValue.toString()
            setTextColor(rightHeaderValue, pddValue)

            val previousValue = viewModel.getPreviousLasValue(position)
            val currentValue = field?.las?.replace(",", ".")?.toDoubleOrNull()

            val direction = ValueDirection.getByValues(currentValue, previousValue)
            updateBackgroundColor(indicator, direction)
        }
    }
}


enum class ValueDirection {
    POSITIVE, NEGATIVE, NEUTRAL;

    companion object {
        fun getByValues(currentValue: Double?, previousValue: Double? = 0.1): ValueDirection {
            if (currentValue ?: 0.0 > previousValue ?: 0.0) {
                return POSITIVE
            } else if (currentValue ?: 0.0 < previousValue ?: 0.0) {
                return NEGATIVE
            } else {
                return NEUTRAL
            }
        }
    }
}


fun TextView.setTextColorRes(@ColorRes colorRes: Int) {
    val color = ContextCompat.getColor(context, colorRes)
    setTextColor(color)
}

fun setTextColor(textView: TextView, pddValue: Double) {
    if (pddValue < 0) {
        textView.setTextColorRes(R.color.red)
    } else {
        textView.setTextColorRes(R.color.green)
    }
}


fun updateBackgroundColor(textView: TextView, valueDirection: ValueDirection) {

    when (valueDirection) {
        ValueDirection.POSITIVE -> {
            textView.setBackgroundColor(
                ContextCompat.getColor(textView.context, R.color.green)
            )
        }

        ValueDirection.NEGATIVE -> {
            textView.setBackgroundColor(
                ContextCompat.getColor(textView.context, R.color.red)
            )
        }

        ValueDirection.NEUTRAL -> {
            textView.setBackgroundColor(
                ContextCompat.getColor(textView.context, R.color.gray)
            )
        }

    }
}