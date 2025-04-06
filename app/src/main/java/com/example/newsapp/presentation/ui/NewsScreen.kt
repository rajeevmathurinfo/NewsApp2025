package com.example.newsapp.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp.R
import com.example.newsapp.domain.model.News
import com.example.newsapp.navigation.Screen
import com.example.newsapp.presentation.viewmodel.NewsViewModel
import com.example.newsapp.presentation.viewmodel.SharedNewsViewModel

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    navController: NavController,
    sharedNewsViewModel: SharedNewsViewModel
) {
    val newsItems = viewModel.news.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(newsItems.itemCount) { index ->
            val newsItem = newsItems[index]
            newsItem?.let {
                NewsItem(it) {
                    sharedNewsViewModel.selectNews(it)
                    navController.navigate(Screen.NewsDetails.route)
                }
            }
        }

        newsItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingIndicator(stringResource(R.string.loading_news)) }
                }

                loadState.append is LoadState.Loading -> {
                    item { LoadingIndicator(stringResource(R.string.loading_more)) }
                }

                loadState.refresh is LoadState.Error -> {
                    item { ErrorText(stringResource(R.string.error_loading_news)) }
                }

                loadState.append is LoadState.Error -> {
                    item { ErrorText(stringResource(R.string.error_loading_more)) }
                }
            }
        }
    }
}

@Composable
fun NewsItem(news: News, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            news.urlToImage?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = news.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(text = news.title ?: stringResource(R.string.no_title), style = MaterialTheme.typography.titleMedium)
            news.publishedAt?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = stringResource(R.string.published, it), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun LoadingIndicator(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.padding(bottom = 8.dp))
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ErrorText(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}
