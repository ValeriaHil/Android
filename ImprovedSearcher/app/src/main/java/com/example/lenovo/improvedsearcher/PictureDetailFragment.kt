package com.example.lenovo.improvedsearcher

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.picture_detail.view.*

class PictureDetailFragment : Fragment() {
    private lateinit var item: Picture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = PictureViewModel.ITEM_MAP[it.getString(ARG_ITEM_ID)] ?: Picture("", "", UnsplashDataLinks(""), UnsplashDataUrls(""))
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.picture_detail, container, false)
        item.let {
            Picasso.get().load(it.links.download).into(rootView.picture_detail)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addToFav = activity?.findViewById<Button>(R.id.favourites) ?: return
        addToFav.setOnClickListener {
            PictureViewModel.insertToDB(item)
        }
        val delFromFav = activity?.findViewById<Button>(R.id.favourites_remove) ?: return
        delFromFav.setOnClickListener {
            PictureViewModel.removeFromDB(item)
        }
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }
}
