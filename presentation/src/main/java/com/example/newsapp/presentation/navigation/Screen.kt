package com.example.newsapp.presentation.navigation

sealed class Screen(val route: String) {
    object NewsList : Screen("news_list")
    object NewsDetails : Screen("news_details")
}




