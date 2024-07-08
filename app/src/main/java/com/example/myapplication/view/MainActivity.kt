package com.example.myapplication.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.service.ApiInterface
import com.example.myapplication.model.service.RetrofitServiceInstance
import com.example.myapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var apiInterface: ApiInterface
    private lateinit var binding: ActivityMainBinding
    // to use viewmodel
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.uiState.observe(this,Observer{state ->

            state.mypageDefaults?.forEach { stock ->
                binding.textViewData.append(" ${stock.cod}, ${stock.gro}, ${stock.tke}, ${stock.def}\n \n ")
            }

            state.myPage?.forEach { column ->
                binding.textView.append(" ${column.name},${column.key} \n \n")
            }
        })

        viewModel.uiStatee.observe(this,Observer{state ->

            state.fields?.forEach { attribute ->
                binding.textView2.append(" ${attribute.tke}, ${attribute.clo}, ${attribute.pdd}, ${attribute.las}\n \n ")
            }

            state.stcs?.forEach { stcs ->
                binding.textView3.append("${stcs}")

            }

        })

        viewModel.getStockList()
        viewModel.getRequestList()
    }

}
