package com.example.myapplication.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.service.ApiInterface
import com.example.myapplication.model.service.RetrofitServiceInstance
import com.example.myapplication.viewmodel.ViewModel
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
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getStockList()
    }

    private fun getStockList() {
        lifecycleScope.launch {
            val response: Response<StockListResponseData> = withContext(Dispatchers.IO) {
                apiInterface.getStockList()
            }

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val data = response.body()
                    binding.textViewData.text = data?.toString() ?: "Veri yok"
                } else {
                    binding.textViewData.text = "API kısmında problem var: ${response.code()}"
                }
            }
        }
    }
}
