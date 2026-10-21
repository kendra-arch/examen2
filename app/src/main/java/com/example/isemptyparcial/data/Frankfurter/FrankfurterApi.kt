package com.example.isemptyparcial.data.Frankfurter

import retrofit2.http.GET
import retrofit2.http.Query

interface FrankfurterApi {
    @GET("v2/rates")
    suspend fun getRates(
        @Query("base") base: String = "USD",
        @Query("quotes") quotes: String = "EUR,BOB"
    ): FrankfurterResponseDto
}
