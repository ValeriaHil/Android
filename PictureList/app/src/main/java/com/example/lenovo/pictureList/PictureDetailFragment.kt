package com.example.lenovo.pictureList

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.lenovo.pictureList.pictures.PicturesContent
import kotlinx.android.synthetic.main.picture_detail.view.*

/**
 * A fragment representing a single Picture detail screen.
 * This fragment is either contained in a [PictureListActivity]
 * in two-pane mode (on tablets) or a [PictureDetailActivity]
 * on handsets.
 */
class PictureDetailFragment : Fragment() {
    private var item: PicturesContent.PictureItem? = null
    private lateinit var view: ImageView
    lateinit var receiver: ServiceReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupServiceReceiver()
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = PicturesContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }
    }

    private fun setupServiceReceiver() {
        receiver = ServiceReceiver(Handler())
        // This is where we specify what happens when data is received from the service
        receiver.setReceiver(object : ServiceReceiver.Receiver {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                if (resultCode == RESULT_OK) {
                    val resultValue = resultData.getByteArray("RESULT_VALUE")
                    val bitmap = BitmapFactory.decodeByteArray(resultValue, 0, resultValue.size)
                    view.setImageBitmap(bitmap)
                }
            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.picture_detail, container, false)
        view = rootView.picture_detail

        val intent = Intent(context, Loader::class.java).apply {
            putExtra("EXTRA_URL", item?.download_link)
            putExtra("RECEIVER", receiver)
        }
        context?.startService(intent)
        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
