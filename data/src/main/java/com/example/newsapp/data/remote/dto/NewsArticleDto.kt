package com.example.newsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO for Articles
data class NewsArticleDto(
    @SerializedName("source") val source: NewsSourceDto,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String?
)