package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.DummyJSON.DummyProductApi
import com.example.isemptyparcial.data.Frankfurter.FrankfurterApi
import com.example.isemptyparcial.data.JSONPlaceholder.JSONPlaceholderApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL_DUMMYJSON = "https://dummyjson.com/"
    private const val BASE_URL_FRANKFURTER = "https://api.frankfurter.dev/"
    private const val BASE_URL_JSONPLACEHOLDER = "https://jsonplaceholder.typicode.com/"

    private val clienteHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val instanceDummyJSON: DummyProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_DUMMYJSON)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DummyProductApi::class.java)
    }

    val instanceFrankfurter: FrankfurterApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_FRANKFURTER)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FrankfurterApi::class.java)
    }

    val instanceJSONPlaceholder: JSONPlaceholderApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_JSONPLACEHOLDER)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JSONPlaceholderApi::class.java)
    }
}
