package com.example.newsapp.domain.model

import com.example.newsapp.data.model.Source

data class News(
    val url: String,
    val author: String?,
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val source: Source?
)
