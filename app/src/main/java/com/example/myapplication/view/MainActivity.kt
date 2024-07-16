package com.example.myapplication.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.uiStateCombined.observe(this, Observer { (stocksResponse, stockRequest) ->
            val stockList = stocksResponse?.mypageDefaults
            val fields = stockRequest?.fields

            if (stockList != null && fields != null) {
                val adapter = StockAdapter(stockList, fields,viewModel)
                binding.recyclerView.adapter = adapter
            }
        })
    }
}
