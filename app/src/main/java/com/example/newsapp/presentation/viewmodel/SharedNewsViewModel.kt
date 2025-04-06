package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.newsapp.domain.model.News

@HiltViewModel
class SharedNewsViewModel @Inject constructor() : ViewModel() {

    private val _selectedNews = MutableStateFlow<News?>(null)
    val selectedNews: StateFlow<News?> = _selectedNews

    fun selectNews(news: News) {
        _selectedNews.value = news
    }

    fun clearSelectedNews() {
        _selectedNews.value = null
    }
}
