package com.example.isemptyparcial.data.DummyJSON

import com.example.isemptyparcial.data.RetrofitClient

class DummyProductRepository {
    suspend fun obtenerProductos(): DummyProductsResponseDto {
        return RetrofitClient.instanceDummyJSON.getProducts()
    }
}
