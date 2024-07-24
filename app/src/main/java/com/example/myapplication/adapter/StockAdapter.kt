package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemStockBinding
import com.example.myapplication.model.Fields
import com.example.myapplication.model.StockListDetail
import com.example.myapplication.viewmodel.convertTurkishDouble


class StockAdapter(
    private var sortType: SortColumnValue = SortColumnValue.CURRENTSORT
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    private var stockListObject = mutableListOf<StockListDetail>()
    private var oldStockResponseObject = mutableListOf<Fields>()
    private var stockResponseObject = mutableListOf<Fields>()

    class StockViewHolder(val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return stockListObject.size
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val currentStock = stockListObject[position]
        val field = stockResponseObject.getOrNull(position)
        val oldField = oldStockResponseObject.getOrNull(position)

        holder.binding.apply {
            name.text = currentStock.cod
            lastUpdateTime.text = field?.clo ?: "-"
            leftHeaderValue.text = field?.las ?: "-"

            val pddString = field?.pdd ?: "-"
            val pddValue = pddString.convertTurkishDouble() ?: 0.0
            rightHeaderValue.text = "%$pddValue"
            setTextColor(rightHeaderValue, pddValue)

            val previousValue = oldField?.las?.convertTurkishDouble()
            val currentValue = field?.las?.convertTurkishDouble()

            val currentClo = field?.clo
            val previousClo = oldField?.clo

            val direction = ValueDirection.getByValues(currentValue, previousValue)
            updateBackgroundColor(indicator, direction)

            highlightedClockValue(recyclerviewItem, currentClo, previousClo)
        }
    }

    fun update(stockList: List<StockListDetail>, stockResponse: List<Fields>, oldStockResponse: List<Fields>, sortType: SortColumnValue) {
        this.sortType = sortType
        stockListObject.clearAndReplace(stockList)
        stockResponseObject.clearAndReplace(stockResponse)
        oldStockResponseObject.clearAndReplace(oldStockResponse)

        val combinedList = stockResponseObject.zip(stockListObject)

        val sortedCombinedList = when (sortType) {
            SortColumnValue.ASCENDING -> combinedList.sortedBy {
                it.first.las?.convertTurkishDouble()
            }
            SortColumnValue.DESCENDING -> combinedList.sortedByDescending {
                it.first.las?.convertTurkishDouble()
            }
            SortColumnValue.DIFFERENCE -> combinedList.sortedBy {
                it.first.pdd?.convertTurkishDouble()
            }
            SortColumnValue.CURRENTSORT -> combinedList
        }
        stockResponseObject.clearAndReplace(sortedCombinedList.map {
            it.first
        })
        stockListObject.clearAndReplace(sortedCombinedList.map {
            it.second
        })
        notifyDataSetChanged()
    }
}

 fun <T> MutableList<T>.clearAndReplace(list: List<T>?) {
     clear()
     list?.let { addAll(it) }
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


fun highlightedClockValue(constraintLayout: ConstraintLayout, currentClo:String?, previousClo:String?){

    if(currentClo != previousClo){
        constraintLayout.setBackgroundColor(
            ContextCompat.getColor(constraintLayout.context, R.color.transparentGray)
        )
    }

    else {
        constraintLayout.setBackgroundColor(
            ContextCompat.getColor(constraintLayout.context, R.color.white)
        )
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

enum class SortColumnValue {
    ASCENDING, DESCENDING, CURRENTSORT, DIFFERENCE;
}