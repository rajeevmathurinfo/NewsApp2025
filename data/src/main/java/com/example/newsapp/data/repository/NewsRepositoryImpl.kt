package com.example.newsapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsapp.data.local.db.NewsDatabase
import com.example.newsapp.data.mapper.toDomain
import com.example.newsapp.data.mediator.NewsRemoteMediator
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDatabase: NewsDatabase
) : NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getTopHeadlines(): Flow<PagingData<News>> {
        val pagingSourceFactory = { newsDatabase.newsDao().getAllNews() }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 1,
                initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(newsApiService, newsDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }
    }
}
