package com.example.lenovo.pictureList.pictures

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.example.lenovo.pictureList.Loader
import com.example.lenovo.pictureList.PictureListActivity
import com.example.lenovo.pictureList.ServiceReceiver
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.StringBuilder
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.math.min

object PicturesContent {

    val ITEMS: MutableList<PictureItem> = ArrayList()
    val ITEM_MAP: MutableMap<String, PictureItem> = HashMap()
    private const val COUNT_OF_ITEMS = 15

    fun loadJson(context: Context, receiver: ServiceReceiver) {
        Loader.load(
            context,
            "https://api.unsplash.com/search/photos/?query=fox&per_page=50&client_id=e8a568ad7a5910210a5c3f94fb63c6e43e4a53eb40a20c4f512def554dd79fe2",
            receiver
        )
    }

    fun parse(data: ByteArray?) {
        val iS = data?.inputStream()
        val scanner = Scanner(iS)
        var dataString = StringBuilder()
        while (scanner.hasNext()) {
            dataString.append(scanner.nextLine())
        }
        val jsn = JsonParser().parse(dataString.toString()) as JsonObject
        val array = jsn.getAsJsonArray("results")
        for (i in 0..min(array.size(), COUNT_OF_ITEMS - 1)) {
            val description = array[i].asJsonObject.get("description").asString
            val download = array[i].asJsonObject.getAsJsonObject("links").get("download").asString
            val preview = array[i].asJsonObject.getAsJsonObject("urls").get("thumb").asString
            addItem(PictureItem(description, download, preview))
        }
    }

    private fun addItem(item: PictureItem) {
        ITEMS.add(item)
        ITEM_MAP[item.description] = item
    }

    data class PictureItem(val description: String, val download_link: String, val preview: String)
}
