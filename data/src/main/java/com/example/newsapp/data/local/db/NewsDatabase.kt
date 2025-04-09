package com.example.newsapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.data.local.dao.NewsDao
import com.example.newsapp.data.local.dao.NewsRemoteKeysDao
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.local.entity.NewsRemoteKeysEntity

@Database(
    entities = [NewsEntity::class, NewsRemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun remoteKeysDao(): NewsRemoteKeysDao
}
