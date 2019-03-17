package com.example.lenovo.improvedsearcher

import android.app.Application
import android.arch.persistence.room.Room

class App : Application() {
    private var database: AppDB? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDB::class.java, "database").build()
    }

    companion object {
        lateinit var instance: App
    }

    fun getDatabase(): AppDB? {
        return database
    }
}