package com.example.newsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.ui.NewsDetailsScreen
import com.example.newsapp.presentation.ui.NewsScreen
import com.example.newsapp.presentation.viewmodel.SharedNewsViewModel

@Composable
fun NewsNavGraph() {
    val navController = rememberNavController()
    val sharedNewsViewModel: SharedNewsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.NewsList.route
    ) {
        composable(route = Screen.NewsList.route) { backStackEntry ->
            NewsScreen(navController = navController, sharedNewsViewModel = sharedNewsViewModel)
        }

        composable(route = "news_details") { backStackEntry ->
            NewsDetailsScreen(
                navController = navController,
                sharedNewsViewModel = sharedNewsViewModel
            )
        }
    }
}


