package com.example.isemptyparcial.data.DummyJSON

import com.google.gson.annotations.SerializedName

data class DummyProductsResponseDto(
    @SerializedName("products") val products: List<DummyProductDto>
)

data class DummyProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("category") val category: String,
    @SerializedName("images") val images: List<String>
)
