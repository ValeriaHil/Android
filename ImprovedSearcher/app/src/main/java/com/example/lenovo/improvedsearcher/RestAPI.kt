package com.example.lenovo.improvedsearcher

import kotlinx.coroutines.Deferred
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApi() {
    private var unsplashApi: UnsplashApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.unsplash.com/search/photos/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        unsplashApi = retrofit.create(UnsplashApi::class.java)
    }

    fun getPictures(): Deferred<Response<UnsplashResponse>> {
        return unsplashApi.messages()
    }
}