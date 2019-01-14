package com.example.lenovo.hw2

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.net.URL
import java.util.*

private const val ACTION_LOAD = "com.example.lenovo.hw2.action.load"
private const val EXTRA_ID = "com.example.lenovo.hw2.extra.id"
private const val EXTRA_URL = "com.example.lenovo.hw2.extra.url "
private val LOG_TAG = Loader::class.java.simpleName

class Loader : IntentService("Loader") {

    private lateinit var callback: Pair<Int, ByteArray>
    private val rnesposes = LinkedList<Pair<Int, ByteArray>>()
    private val main = Handler(Looper.getMainLooper())

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_LOAD -> {
                val id = intent.getIntExtra(EXTRA_ID, -1)
                val url = intent.getStringExtra(EXTRA_URL)
                loadPicture(id, url)
            }
        }
    }

    private fun loadPicture(id: Int, urlStr: String) {
        var res: ByteArray?
        try {
            val url = URL(urlStr)
            val connection = url.openConnection()
            connection.connect()
            res = ByteArray(connection.contentLength)
            connection.getInputStream().use { i ->
                var p = 0
                var r: Int
                while (true) {
                    r = i.read(res, p, res!!.size - p)
                    if (r <= 0) {
                        break
                    }
                    p += r
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            res = null
        }

        Log.d(LOG_TAG, "loadPicture: got: id: " + id + ", data.length = " + res?.size)
//        main.post({ deliver(id, res) })
    }

    companion object {
        // TODO: Customize helper method
        @JvmStatic
        fun load(context: Context, id: Int, url: String) {
            val intent = Intent(context, Loader::class.java).apply {
                action = ACTION_LOAD
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_URL, url)
            }
            context.startService(intent)
        }
    }

//    private fun deliver(id: Int, data: ByteArray) {
//        if (callback.first != -1)
//            callback = Pair(id, data)
//        else
//            responses.add(Pair(id, data))
//    }

//    override fun onBind(intent: Intent): MyBinder {
//        Log.d(LOG_TAG, "onBind: ")
//        return MyBinder()
//    }

//    override fun onUnbind(intent: Intent): Boolean {
//        Log.d(LOG_TAG, "onUnbind: ")
//        callback.first = -1
//        return super.onUnbind(intent)
//    }

    override fun onRebind(intent: Intent) {
        Log.d(LOG_TAG, "onRebind: ")
        super.onRebind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "onCreate: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "onDestroy: ")
    }

//    private class myBinder: Binder() {
//        var service = Loader()
//        fun setCallback(callback: Pair<Int, ByteArray>) {
//            Handler(Looper.getMainLooper()).post {
//                service.callback = callback
//                while(!service.responses.isEmpty()) {
//                    service.callback = service.responses.remove()
//                }
//            }
//        }
//    }
}

