package com.example.newsapp.data.mapper

import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.domain.model.Source
import com.example.newsapp.data.remote.dto.NewsArticleDto
import com.example.newsapp.data.remote.dto.NewsSourceDto
import com.example.newsapp.domain.model.News

fun NewsArticleDto.toEntity(): NewsEntity {
    return NewsEntity(
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        source = source.toSource()
    )
}

fun NewsSourceDto.toSource(): Source {
    return Source(
        id = id,
        name = name
    )
}


// Maps NewsEntity to domain model News
fun NewsEntity.toDomain(): News {
    return News(
        url = url,
        author = author,
        title = title,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        source = source
    )
}



