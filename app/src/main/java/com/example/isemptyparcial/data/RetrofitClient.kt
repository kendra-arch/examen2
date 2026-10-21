package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.kanye.KanyeQuoteApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL_KANYE = "https://api.kanye.rest/"

    private val clienteHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val instanceKanye: KanyeQuoteApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_KANYE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KanyeQuoteApi::class.java)
    }
}
