package com.example.lenovo.improvedsearcher

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

@Entity
@TypeConverters(Converter::class)
class Picture(
    @PrimaryKey
    val id: String,
    val alt_description: String?,
    val links: UnsplashDataLinks,
    val urls: UnsplashDataUrls
)

class UnsplashDataUrls(
    val thumb: String
)

class UnsplashDataLinks(
    val download: String
)