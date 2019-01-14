package com.example.lenovo.hw2

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.lenovo.hw2.pictures.PicturesContent
import kotlinx.android.synthetic.main.activity_picture_list.*
import kotlinx.android.synthetic.main.picture_list_content.view.*
import kotlinx.android.synthetic.main.picture_list.*
import android.widget.ImageView
import com.squareup.picasso.Picasso
import android.support.v4.content.ContextCompat


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [PictureDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
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

    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(this, PicturesContent.ITEMS, twoPane)
    }

    class RecyclerViewAdapter(
        private val parentActivity: PictureListActivity,
        private val values: List<PicturesContent.PictureItem>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as PicturesContent.PictureItem
                if (twoPane) {
                    val fragment = PictureDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(PictureDetailFragment.ARG_ITEM_ID, item.description)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.picture_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, PictureDetailActivity::class.java).apply {
                        putExtra(PictureDetailFragment.ARG_ITEM_ID, item.description)
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
            val item = values[position]
            holder.contentView.text = item.description
            ContextCompat.checkSelfPermission(parentActivity, "INTERNET")
            Picasso.get().load(item.preview).into(holder.imageView)
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val contentView: TextView = view.content
            val imageView : ImageView = view.pictureImageView
        }
    }
}
