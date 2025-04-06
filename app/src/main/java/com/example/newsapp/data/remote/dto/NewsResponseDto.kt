package com.example.newsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO for API Response
data class NewsResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<NewsArticleDto>
)