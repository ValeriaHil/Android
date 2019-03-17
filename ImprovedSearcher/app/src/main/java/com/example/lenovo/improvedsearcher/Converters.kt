package com.example.lenovo.improvedsearcher

import android.arch.persistence.room.TypeConverter

class Converter {
    @TypeConverter
    fun fromUrls(urls: UnsplashDataUrls): String {
        return urls.thumb
    }

    @TypeConverter
    fun toUrls(data: String): UnsplashDataUrls {
        return UnsplashDataUrls(data)
    }
    @TypeConverter
    fun fromLinks(urls: UnsplashDataLinks): String {
        return urls.download
    }

    @TypeConverter
    fun toLinks(data: String): UnsplashDataLinks {
        return UnsplashDataLinks(data)
    }
}
