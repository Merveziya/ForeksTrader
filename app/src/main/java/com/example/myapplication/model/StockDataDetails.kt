package com.example.myapplication.model

data class StockDataDetails (
    val name: String,
    val lastUpdateTime: String?,
    val selectedLeftFieldValue: String?,
    val selectedRightFieldValue: String?,
    val priceChange: String?,
    val price: String?,
    val highlight: Boolean
)
