package com.example.isemptyparcial.data.kanye

import com.example.isemptyparcial.data.RetrofitClient

class KanyeQuoteRepository {
    suspend fun obtenerCItaKanyeAleatoria(): KanyeQuoteDto {
        return RetrofitClient.instanceKanye.getRandomKanyeQuote()
    }
}
