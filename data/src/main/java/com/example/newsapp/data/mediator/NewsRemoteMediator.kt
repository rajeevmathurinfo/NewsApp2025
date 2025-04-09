package com.example.newsapp.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.RemoteMediator
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.example.newsapp.data.local.db.NewsDatabase
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.local.entity.NewsRemoteKeysEntity
import com.example.newsapp.data.mapper.toEntity
import com.example.newsapp.data.remote.api.NewsApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDatabase: NewsDatabase
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                val remoteKey = lastItem?.let {
                    newsDatabase.remoteKeysDao().getRemoteKeyByArticleUrl(it.url) // use url now
                }
                remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val response =
                newsApiService.getTopHeadlines(page = page, pageSize = state.config.pageSize)
            val articles = response.articles.map { it.toEntity() }
            //Log.d("Mediator", "LoadType: $loadType, Page: $page, Articles: ${articles.size}")

            val endOfPaginationReached = articles.isEmpty()

            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDatabase.newsDao().clearAll()
                    newsDatabase.remoteKeysDao().clearRemoteKeys()
                }

                val keys = articles.map {
                    NewsRemoteKeysEntity(
                        articleUrl = it.url,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }

                newsDatabase.remoteKeysDao().insertAll(keys)
                newsDatabase.newsDao().insertAll(articles)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}

