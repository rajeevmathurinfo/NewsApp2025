package com.example.newsapp.presentation.viewmodel

import androidx.paging.*
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.usecase.NewsUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.example.newsapp.testutils.TestPagingHelpers.newsEntityDiffCallback
import com.example.newsapp.testutils.TestPagingHelpers.noopListCallback


@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsUseCase: NewsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        newsUseCase = mockk()
    }

    @Test
    fun `news flow emits expected PagingData`() = runTest {
        val dummyNews = listOf(
            News("url1", "Author 1", "Title 1", "Desc 1", null, null, null, null),
            News("url2", "Author 2", "Title 2", "Desc 2", null, null, null, null)
        )

        val pagingData = PagingData.from(dummyNews)

        // ✅ Mock BEFORE ViewModel init
        every { newsUseCase.invoke() } returns flowOf(pagingData)

        viewModel = NewsViewModel(newsUseCase)

        val differ = AsyncPagingDataDiffer(
            diffCallback = newsEntityDiffCallback,
            updateCallback = noopListCallback,
            mainDispatcher = this@runTest.coroutineContext,
            workerDispatcher = this@runTest.coroutineContext
        )


        val job = launch {
            viewModel.news.collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        val snapshot = differ.snapshot()
        assertEquals(2, snapshot.size)
        assertEquals("url1", snapshot[0]?.url)

        job.cancel()
    }

}



