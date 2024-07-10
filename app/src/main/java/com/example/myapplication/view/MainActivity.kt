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

        viewModel.uiState.observe(this, Observer { state ->
            state.mypageDefaults?.let { stocks ->
                val stockList = stocks

                viewModel.uiStatee.observe(this, Observer { responseState ->
                    responseState.fields?.let { fields ->
                        val stockRequest = fields
                        val adapter = StockAdapter(stockList, stockRequest)
                        binding.recyclerView.adapter = adapter
                    }
                })
            }
        })

    }
}
