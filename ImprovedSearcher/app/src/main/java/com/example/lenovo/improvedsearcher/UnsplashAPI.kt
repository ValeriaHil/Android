package com.example.lenovo.improvedsearcher

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

data class UnsplashResponse(
    val results: List<Picture>
)

interface UnsplashApi {
    @GET("?query=fox&per_page=50&client_id=e8a568ad7a5910210a5c3f94fb63c6e43e4a53eb40a20c4f512def554dd79fe2")
    fun messages(): Deferred<Response<UnsplashResponse>>
}