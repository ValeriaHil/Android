package com.example.lenovo.improvedsearcher

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object PictureViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository: PictureRepository = PictureRepository()

    val picturesLiveData = MutableLiveData<List<Picture>>()
    val favouritesLiveData = MutableLiveData<List<Picture>>()

    val ITEM_MAP: MutableMap<String?, Picture> = HashMap()

    fun receivePictures() {
        scope.launch {
            val pictures = repository.getPhotos()
            pictures?.forEach {
                ITEM_MAP[it.id] = it
            }
            picturesLiveData.postValue(pictures)
        }
    }

    fun insertToDB(item: Picture) {
        scope.launch {
            App.instance.getDatabase()!!.pictureDao().insert(item)
        }
    }

    fun removeFromDB(item: Picture) {
        scope.launch {
            App.instance.getDatabase()!!.pictureDao().delete(item)
        }
    }
    fun getFav() {
        scope.launch {
            val res = App.instance.getDatabase()!!.pictureDao().getAll()
            favouritesLiveData.postValue(res)
        }
    }
}