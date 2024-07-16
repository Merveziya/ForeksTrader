package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Fields(
    val tke: String?,
    val las: String?,
    val pdd: String?,
    val ddi: String?,
    val low: String?,
    val hig: String?,
    val buy: String?,
    val sel: String?,
    val pdc: String?,
    val cei: String?,
    val flo: String?,
    val gco: String?,
    val clo: String?,
)

data class StockRequestData(
    @SerializedName("l") val fields: List<Fields>? = null,
    @SerializedName("z") val z: Int = 0,
)



