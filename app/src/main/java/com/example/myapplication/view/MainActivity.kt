package com.example.myapplication.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.StockAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.service.ApiInterface
import com.example.myapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var apiInterface: ApiInterface
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var pddValue = "pdd"
    private var lasValue = "las"

    private var dropdownNameList = arrayListOf<String>()
    private var dropdownKeyList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val criterion = resources.getStringArray(R.array.criterion)
        dropdownNameList.addAll(criterion)

        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_item,dropdownNameList)
        binding.firstAutoCompleteTextView.setAdapter(arrayAdapter)
        binding.lastAutoCompleteTextView.setAdapter(arrayAdapter)


        //tıklandığında seçilen alanı kaydediyor
        binding.firstAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            lasValue = dropdownKeyList.getOrNull(position) ?: "las"
        }

        binding.lastAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            pddValue = dropdownKeyList.getOrNull(position) ?: "las"
        }

        viewModel.combinedLiveData.observe(this, Observer { (stocksResponse, mainResponseData) ->
            val stockList = stocksResponse?.mypageDefaults
            val currentFields = mainResponseData.currentResponseData.fields
            val oldFields = mainResponseData.oldStocklistResponseData.fields

            if (stockList != null && currentFields != null && oldFields != null) {
                val adapter = StockAdapter(stockList, currentFields, oldFields)
                binding.recyclerView.adapter = adapter
            }
        })
    }
}
