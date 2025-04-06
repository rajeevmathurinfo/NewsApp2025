package com.example.newsapp.domain.usecase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.model.Source
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.testutils.TestPagingHelpers.newsEntityDiffCallback
import com.example.newsapp.testutils.TestPagingHelpers.noopListCallback
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsUseCaseTest {
    private lateinit var repository: NewsRepository
    private lateinit var useCase: NewsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = NewsUseCase(repository)
    }

    @Test
    fun `invoke should return PagingData of NewsEntity from repository`() = runTest {
        val dummyNews = listOf(
            News(
                url = "https://example.com/article1",
                author = "Author 1",
                title = "Test Title",
                description = "Test description",
                urlToImage = "https://example.com/image.jpg",
                publishedAt = "2025-04-02T00:00:00Z",
                content = "Some content",
                source = Source(id = "cnn", name = "CNN")
            )
        )

        val expectedPagingData = PagingData.from(dummyNews)
        coEvery { repository.getTopHeadlines() } returns flowOf(expectedPagingData)

        // When
        val result = useCase()

        // Then
        result.test {
            val emitted = awaitItem()

            // Use AsyncPagingDataDiffer to extract data
            val differ = AsyncPagingDataDiffer(
                diffCallback = newsEntityDiffCallback,
                updateCallback = noopListCallback,
                mainDispatcher = this@runTest.coroutineContext,
                workerDispatcher = this@runTest.coroutineContext
            )

            differ.submitData(emitted)
            advanceUntilIdle() // Required to wait for submitData to complete

            val snapshot = differ.snapshot()
            assertThat(snapshot.size).isEqualTo(dummyNews.size)
            assertThat(snapshot[0]?.url).isEqualTo("https://example.com/article1")
            cancelAndIgnoreRemainingEvents()
        }
    }

}
