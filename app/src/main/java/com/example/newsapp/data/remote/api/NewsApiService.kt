package com.example.newsapp.data.remote.api

import com.example.newsapp.data.remote.dto.NewsResponseDto
import com.example.newsapp.utill.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = API_KEY // we can secure api key in gradle.properties
    ): NewsResponseDto
}
