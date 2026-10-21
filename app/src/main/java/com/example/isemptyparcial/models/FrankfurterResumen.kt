package com.example.isemptyparcial.models

data class FrankfurterResumen(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
