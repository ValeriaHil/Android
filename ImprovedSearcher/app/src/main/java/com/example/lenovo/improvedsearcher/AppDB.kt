package com.example.lenovo.improvedsearcher

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase


@Database(entities = [Picture::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun pictureDao(): PictureDao
}