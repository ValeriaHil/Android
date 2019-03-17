package com.example.lenovo.improvedsearcher

class PictureRepository : BaseRepository() {
    suspend fun getPhotos(): List<Picture>? {
        val pictureResponse = safeApiCall(
            call = { RestApi().getPictures().await() },
            errorMessage = "Error Receiving Photos"
        )

        return pictureResponse?.results?.toMutableList();
    }
}