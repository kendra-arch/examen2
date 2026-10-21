package com.example.isemptyparcial.data.DummyJSON

import retrofit2.http.GET

interface DummyProductApi {
    @GET("products")
    suspend fun getProducts(): DummyProductsResponseDto
}
