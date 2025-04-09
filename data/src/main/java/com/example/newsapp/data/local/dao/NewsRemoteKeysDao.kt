package com.example.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.NewsRemoteKeysEntity

@Dao
interface NewsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<NewsRemoteKeysEntity>)

    @Query("SELECT * FROM news_remote_keys WHERE articleUrl = :url")
    suspend fun getRemoteKeyByArticleUrl(url: String): NewsRemoteKeysEntity?

    @Query("DELETE FROM news_remote_keys")
    suspend fun clearRemoteKeys()
}


