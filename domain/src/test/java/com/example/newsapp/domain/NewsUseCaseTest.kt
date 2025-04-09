package com.example.newsapp.domain

import androidx.paging.PagingData
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.usecase.NewsUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsUseCaseTest {

    private lateinit var newsUseCase: NewsUseCase
    private val newsRepository: NewsRepository = mockk()

    @Before
    fun setUp() {
        newsUseCase = NewsUseCase(newsRepository)
    }

    @Test
    fun `invoke should return PagingData of News from repository`() = runTest {
        val dummyNews = listOf(
            News("url1", "Author 1", "Title 1", "Desc 1", null, null, null, null),
            News("url2", "Author 2", "Title 2", "Desc 2", null, null, null, null)
        )
        val pagingData = PagingData.from(dummyNews)

        every { newsRepository.getTopHeadlines() } returns flowOf(pagingData)

        // When
        val resultFlow = newsUseCase()
        val result = resultFlow

        // Then
        result.collect {
            verify(exactly = 1) { newsRepository.getTopHeadlines() }
        }
    }
}
