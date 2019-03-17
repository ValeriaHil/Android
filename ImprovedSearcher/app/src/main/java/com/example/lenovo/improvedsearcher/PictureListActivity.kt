package com.example.lenovo.improvedsearcher

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_picture_list.*
import kotlinx.android.synthetic.main.picture_list_content.view.*
import kotlinx.android.synthetic.main.picture_list.*
import android.widget.ImageView
import com.squareup.picasso.Picasso

class PictureListActivity : AppCompatActivity() {

    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (picture_detail_container != null) {
            twoPane = true
        }

        setupRecyclerView(picture_list)

        val goToFav = findViewById<Button>(R.id.go_to_favourites)
        goToFav.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
        }
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        PictureViewModel.receivePictures()
        PictureViewModel.picturesLiveData.observe(this, Observer {
            recyclerView.adapter = RecyclerViewAdapter(this, it ?: emptyList(), twoPane)
        })
    }

    class RecyclerViewAdapter(
        private val parentActivity: PictureListActivity,
        private val values: List<Picture>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Picture
                if (twoPane) {
                    val fragment = PictureDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(PictureDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.picture_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, PictureDetailActivity::class.java).apply {
                        putExtra(PictureDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(values[position])
            with(holder.itemView) {
                tag = values[position]
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            fun bind(item: Picture) {
                contentView.text = item.alt_description
                Picasso.get().load(item.urls.thumb).into(imageView)
            }

            private val contentView: TextView = view.content
            private val imageView: ImageView = view.pictureImageView
        }
    }
}
