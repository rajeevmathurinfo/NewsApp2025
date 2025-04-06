package com.example.newsapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// DTO for Source
data class NewsSourceDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String
)