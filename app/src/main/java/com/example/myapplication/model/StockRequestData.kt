package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class Fields(
@SerializedName("tke") val tke: String? = null,
@SerializedName("clo") val clo: String? = null,
@SerializedName("pdd") val pdd: String? = null,
@SerializedName("las") val las: String? = null
)

data class StockRequestData(
    @SerializedName("l") val fields: List<Fields>? = null,
    @SerializedName("z") val stcs: String? = null,
    val converterText2: String? = null
)

