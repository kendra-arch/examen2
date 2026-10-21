package com.example.isemptyparcial.data.JSONPlaceholder

import retrofit2.http.GET

interface JSONPlaceholderApi {
    @GET("posts")
    suspend fun getPosts(): List<PostDto>
}
