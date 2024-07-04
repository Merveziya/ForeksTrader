package com.example.myapplication.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.StockListResponseData
import com.example.myapplication.model.service.ApiInterface
import com.example.myapplication.model.service.RetrofitServiceInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getStockList()
    }

    private fun getStockList() {
        lifecycleScope.launch {
            val apiInterface = RetrofitServiceInstance.getInstance().create(ApiInterface::class.java)
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
