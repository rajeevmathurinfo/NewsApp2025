package com.example.newsapp.domain.usecase

import androidx.paging.PagingData
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<PagingData<News>> {
        return repository.getTopHeadlines()
    }
}
