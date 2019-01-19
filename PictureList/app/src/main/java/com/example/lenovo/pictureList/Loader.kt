package com.example.lenovo.pictureList

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.os.*
import android.util.Log
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class Loader : IntentService("Loader") {

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra("EXTRA_URL")
        val rec = intent?.getParcelableExtra("RECEIVER") as ResultReceiver
        val bundle = Bundle()
        bundle.putByteArray("RESULT_VALUE", loadByteArray(url))
        rec.send(Activity.RESULT_OK, bundle)
    }

    private fun loadByteArray(url: String?): ByteArray {
        Log.d(logTag, "Downloading from ${url}")
        return URL(url).openConnection().run {
            Log.d(logTag, "Opened Connection")
            connect()
            Log.d(logTag, "Connected")
            val code = (this as? HttpURLConnection)?.responseCode
            Log.d(logTag, "Response code: $code")
            val buffer = ByteArrayOutputStream()
            getInputStream().use { i ->
                val data = ByteArray(16384)
                var cntRead: Int
                while (true) {
                    cntRead = i.read(data, 0, data.size)
                    if (cntRead != -1) {
                        buffer.write(data, 0, cntRead)
                    } else {
                        break
                    }
                }
            }
            buffer.toByteArray()
        }
    }

    companion object {
        @JvmStatic
        fun load(context: Context, url: String, receiver: ServiceReceiver) {
            val intent = Intent(context, Loader::class.java).apply {
                putExtra("EXTRA_URL", url)
                putExtra("RECEIVER", receiver)
            }
            context.startService(intent)
        }
        private const val logTag = "Loader"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(logTag, "onCreate: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(logTag, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy: ")
    }
}
