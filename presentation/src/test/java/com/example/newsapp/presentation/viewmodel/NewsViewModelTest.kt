import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.example.newsapp.domain.model.News
import com.example.newsapp.domain.usecase.NewsUseCase
import com.example.newsapp.presentation.viewmodel.NewsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)

class NewsViewModelTest {

    private lateinit var newsUseCase: NewsUseCase
    private lateinit var viewModel: NewsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        newsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `news flow emits expected PagingData from use case`() = runTest {
        // Given
        val dummyNews = listOf(
            News(
                url = "https://example.com/news1",
                author = "Author1",
                title = "Title1",
                description = "Description1",
                urlToImage = "https://example.com/image1.jpg",
                publishedAt = "2024-04-09T00:00:00Z",
                content = "Content1",
                source = null
            )
        )

        val pagingData = PagingData.from(dummyNews)
        coEvery { newsUseCase.invoke() } returns flowOf(pagingData)

        // When
        viewModel = NewsViewModel(newsUseCase)

        // Then
        viewModel.news.test {
            val emittedData = awaitItem()

            val differ = AsyncPagingDataDiffer(
                diffCallback = NewsDiffCallback(),
                updateCallback = NoopListCallback(),
                mainDispatcher = testDispatcher,
                workerDispatcher = testDispatcher
            )

            differ.submitData(emittedData)
            advanceUntilIdle()

            assertEquals(1, differ.snapshot().size)
            assertEquals(dummyNews[0], differ.snapshot()[0])

            cancelAndIgnoreRemainingEvents()
        }
    }

    private class NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    private class NewsDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }
}
