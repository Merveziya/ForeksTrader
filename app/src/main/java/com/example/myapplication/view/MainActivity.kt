package com.example.myapplication.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.SortColumnValue
import com.example.myapplication.adapter.StockAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.service.ApiInterface
import com.example.myapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var apiInterface: ApiInterface
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: StockAdapter

    private var isSorting = false

    private var dropdownNameList = arrayListOf<String>()

    override fun onPause() {
        super.onPause()
        viewModel.onActivityStopped()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.updateResponseData(this, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val criterion = resources.getStringArray(R.array.criterion)
        dropdownNameList.addAll(criterion)

        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, dropdownNameList)
        binding.firstAutoCompleteTextView.setAdapter(arrayAdapter)
        binding.lastAutoCompleteTextView.setAdapter(arrayAdapter)

        // Adapter setlendi
        adapter = StockAdapter()
        binding.recyclerView.adapter = adapter

        binding.firstAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedCriterion = criterion[position]
            val sortOrder = when (selectedCriterion) {
                "Yüksek" -> SortColumnValue.ASCENDING
                "Düşük" -> SortColumnValue.DESCENDING
                else -> SortColumnValue.CURRENTSORT
            }

            sortOrder?.let {
                isSorting = true
                viewModel.combinedLiveData.observe(this@MainActivity, Observer { (stocksResponse, mainResponseData) ->
                    val stockList = stocksResponse?.mypageDefaults
                    val currentFields = mainResponseData.currentResponseData.fields
                    val oldFields = mainResponseData.oldStocklistResponseData.fields

                    if (stockList != null && currentFields != null && oldFields != null) {
                        adapter.update(stockList, currentFields, oldFields, sortOrder)
                    }
                })
            }
        }


        binding.lastAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
               val selectedCriterion = criterion[position]
               val sortOrder = when(selectedCriterion){
                   "Yüksek" -> SortColumnValue.ASCENDING
                   "Düşük"  -> SortColumnValue.DESCENDING
                   else -> SortColumnValue.CURRENTSORT
               }

            sortOrder?.let {
                isSorting = true
                viewModel.combinedLiveData.observe(this@MainActivity, Observer { (stocksResponse, mainResponseData) ->
                    val stockList = stocksResponse?.mypageDefaults
                    val currentFields = mainResponseData.currentResponseData.fields
                    val oldFields = mainResponseData.oldStocklistResponseData.fields

                    if (stockList != null && currentFields != null && oldFields != null) {
                        adapter.update(stockList, currentFields, oldFields, sortOrder)
                    }
                })
            }
        }

        viewModel.combinedLiveData.observe(this, Observer { (stocksResponse, mainResponseData) ->

                val stockList = stocksResponse?.mypageDefaults
                val currentFields = mainResponseData.currentResponseData.fields
                val oldFields = mainResponseData.oldStocklistResponseData.fields

                if (stockList != null && currentFields != null && oldFields != null) {
                    adapter.update(stockList, currentFields, oldFields, SortColumnValue.CURRENTSORT)
                }

        })
    }
}
