package com.example.newsapp.data.local.db

import androidx.room.TypeConverter
import com.example.newsapp.domain.model.Source
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        return sourceString?.let {
            Gson().fromJson(it, Source::class.java)
        }
    }
}

