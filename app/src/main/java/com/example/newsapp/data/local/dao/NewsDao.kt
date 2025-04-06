package com.example.newsapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.NewsEntity

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsEntity>)

    @Query("SELECT * FROM news")
    fun getAllNews(): PagingSource<Int, NewsEntity>

    @Query("DELETE FROM news")
    suspend fun clearAll()
}
