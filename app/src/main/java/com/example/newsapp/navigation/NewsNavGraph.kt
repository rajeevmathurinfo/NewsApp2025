package com.example.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.ui.NewsDetailsScreen
import com.example.newsapp.presentation.ui.NewsScreen
import com.example.newsapp.presentation.viewmodel.SharedNewsViewModel

@Composable
fun NewsNavGraph(sharedNewsViewModel: SharedNewsViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NewsList.route
    ) {
        composable(route = Screen.NewsList.route) {
            NewsScreen(navController = navController, sharedNewsViewModel = sharedNewsViewModel)
        }

        composable(
            route = "news_details",
        ) {
            NewsDetailsScreen(
                navController = navController,
                sharedNewsViewModel = sharedNewsViewModel
            )
        }
    }
}

