package com.example.lenovo.improvedsearcher

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favourite_content.view.*

class FavouritesListFragment : Fragment() {

    private var listener: OnItemSelected? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnItemSelected) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recycler_for_favourites, container, false)
    }

    interface OnItemSelected {
        fun onItemSelected(id: String)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = activity?.findViewById<RecyclerView>(R.id.recycler_for_favourites) ?: return
        recycler.layoutManager = GridLayoutManager(context, 3)
        PictureViewModel.getFav()
        PictureViewModel.favouritesLiveData.observe(this, Observer {
            recycler.adapter = Adapter(it ?: emptyList())
        })
    }

    inner class Adapter(private val pictures: List<Picture>) :
        RecyclerView.Adapter<Adapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Picture
                listener?.onItemSelected(item.id)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.favourite_content, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return pictures.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(pictures[position])
            with(holder.itemView) {
                tag = pictures[position]
                setOnClickListener(onClickListener)
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(item: Picture) {
                Picasso.get().load(item.urls.thumb).into(imageView)
            }

            private val imageView: ImageView = view.favourite_image
        }
    }
}