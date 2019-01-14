package com.example.lenovo.hw2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lenovo.hw2.pictures.PicturesContent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.picture_detail.view.*

/**
 * A fragment representing a single Picture detail screen.
 * This fragment is either contained in a [PictureListActivity]
 * in two-pane mode (on tablets) or a [PictureDetailActivity]
 * on handsets.
 */
class PictureDetailFragment : Fragment() {
    private var item: PicturesContent.PictureItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = PicturesContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.picture_detail, container, false)
        item?.let {
            ContextCompat.checkSelfPermission(this.context!!, "INTERNET")
            Picasso.get().load(it.download_link).into(rootView.picture_detail)
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
