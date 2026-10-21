package com.example.isemptyparcial.data.Frankfurter

import com.example.isemptyparcial.data.RetrofitClient

class FrankfurterRepository {
    suspend fun obtenerRates(base: String = "USD", quotes: String = "EUR,BOB"): FrankfurterResponseDto {
        return RetrofitClient.instanceFrankfurter.getRates(base, quotes)
    }
}
