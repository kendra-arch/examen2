package com.example.isemptyparcial.data.Frankfurter

import com.google.gson.annotations.SerializedName

data class FrankfurterResponseDto(
    @SerializedName("base") val base: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("rates") val rates: Map<String, Double>?
)
