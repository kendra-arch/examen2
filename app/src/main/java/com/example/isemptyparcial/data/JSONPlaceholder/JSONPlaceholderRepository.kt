package com.example.isemptyparcial.data.JSONPlaceholder

import com.example.isemptyparcial.data.RetrofitClient

class JSONPlaceholderRepository {
    suspend fun obtenerPosts(): List<PostDto> {
        return RetrofitClient.instanceJSONPlaceholder.getPosts()
    }
}
