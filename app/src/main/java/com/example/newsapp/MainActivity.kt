package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.newsapp.navigation.NewsNavGraph
import com.example.newsapp.presentation.ui.NewsScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //testApiCall()
        setContent {
            NewsAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NewsNavGraph()
                }
            }

        }
    }

/*    private fun testApiCall() {
        lifecycleScope.launch {
            try {
                val response = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(NewsApiService::class.java)
                    .getNews(page = 1, pageSize = 10)

                // ✅ Print full response to check if "articles" exist
                Log.d("API_TEST", "Full Response: $response")

                Log.d("API_TEST", "News Loaded: ${response.articles.size} articles")
            } catch (e: Exception) {
                Log.e("API_TEST", "Error fetching news", e)
            }
        }
    }*/

}
