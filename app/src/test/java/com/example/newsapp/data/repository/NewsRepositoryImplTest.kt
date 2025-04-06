package com.example.newsapp.data.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.local.dao.NewsDao
import com.example.newsapp.data.local.db.NewsDatabase
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.remote.api.NewsApiService
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.testutils.TestPagingUtils
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var newsApiService: NewsApiService
    private lateinit var newsDatabase: NewsDatabase
    private lateinit var newsDao: NewsDao
    private lateinit var repository: NewsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        newsApiService = mockk(relaxed = true)
        newsDatabase = mockk(relaxed = true)
        newsDao = mockk(relaxed = true)

        every { newsDatabase.newsDao() } returns newsDao

        every { newsDatabase.runInTransaction(any<Runnable>()) } answers {
            firstArg<Runnable>().run()
        }

        repository = NewsRepositoryImpl(newsApiService, newsDatabase)
    }


    @Test
    fun `getTopHeadlines returns PagingData from local source`() = runTest {
        // Dummy data
        val dummyNews = listOf(
            NewsEntity("url1", "Author 1", "Title 1", "Desc 1", null, null, null, null),
            NewsEntity("url2", "Author 2", "Title 2", "Desc 2", null, null, null, null)
        )

        val pagingSourceFactory = { DummyPagingSource(dummyNews) }

        every { newsDao.getAllNews() } returns pagingSourceFactory()

        val flow = repository.getTopHeadlines()

        val differ = AsyncPagingDataDiffer(
            diffCallback = TestPagingUtils.diffCallback,
            updateCallback = TestPagingUtils.noopListCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        val job = launch {
            flow.collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        val snapshot = differ.snapshot()
        assertThat(snapshot.size).isEqualTo(dummyNews.size)
        assertThat(snapshot[0]?.url).isEqualTo("url1")

        job.cancel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Dummy PagingSource for testing
    class DummyPagingSource(private val items: List<NewsEntity>) : PagingSource<Int, NewsEntity>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsEntity> {
            return LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = null
            )
        }

        override fun getRefreshKey(state: PagingState<Int, NewsEntity>): Int? = null
    }

}
