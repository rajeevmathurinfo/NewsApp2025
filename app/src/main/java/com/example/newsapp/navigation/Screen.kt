package com.example.newsapp.navigation

sealed class Screen(val route: String) {
    object NewsList : Screen("news_list")
    object NewsDetails : Screen("news_details")
}




