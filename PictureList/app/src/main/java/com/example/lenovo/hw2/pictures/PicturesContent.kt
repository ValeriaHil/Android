package com.example.lenovo.hw2.pictures

import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap
import javax.net.ssl.HttpsURLConnection
import kotlin.math.min

object PicturesContent {

    val ITEMS: MutableList<PictureItem> = ArrayList()

    val ITEM_MAP: MutableMap<String, PictureItem> = HashMap()

    private val COUNT = 15

    init {
        val downloadTask = DownloadPicturesAsyncTask(WeakReference(this))
        downloadTask.execute(URL("https://api.unsplash.com/search/photos/?query=fox&per_page=50&client_id=e8a568ad7a5910210a5c3f94fb63c6e43e4a53eb40a20c4f512def554dd79fe2"))
        val pictureJson = downloadTask.get()
        val array = pictureJson.getAsJsonArray("results")
        for(i in 0..min(array.size(), COUNT - 1)) {
            val description = array[i].asJsonObject.get("description").asString
            val download = array[i].asJsonObject.getAsJsonObject("links").get("download").asString
            val preview = array[i].asJsonObject.getAsJsonObject("urls").get("thumb").asString
            ITEMS.add(PictureItem(description, download, preview))
            ITEM_MAP.put(description, PictureItem(description, download, preview))
        }
    }

    private fun addItem(item: PictureItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.description, item)
    }


    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class PictureItem(val description: String, val download_link: String, val preview: String)

    private class DownloadPicturesAsyncTask(val activity: WeakReference<PicturesContent>) : AsyncTask<URL, Unit, JsonObject>() {

        private val logTag = "ASYNC_TASK"
        private lateinit var jsonResponse: JsonObject

        override fun doInBackground(vararg params: URL) : JsonObject {
            Log.d(logTag, "Downloading from ${params[0].toString()}")

            val response = params[0].openConnection().run {
                Log.d(logTag, "Opened Connection")
                connect()
                Log.d(logTag, "Connected")
                val code = (this as? HttpURLConnection)?.responseCode
                Log.d(logTag, "Response code: $code")

                getInputStream().bufferedReader().readLines().joinToString("")
            }
            val js = JsonParser().parse(response)
            jsonResponse = js.asJsonObject
            Log.d(logTag, "Response = ${jsonResponse.toString()}")
            return jsonResponse
        }

    }
}
