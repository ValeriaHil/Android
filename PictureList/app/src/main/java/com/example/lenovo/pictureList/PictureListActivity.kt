package com.example.lenovo.pictureList

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.lenovo.pictureList.pictures.PicturesContent
import kotlinx.android.synthetic.main.activity_picture_list.*
import kotlinx.android.synthetic.main.picture_list_content.view.*
import kotlinx.android.synthetic.main.picture_list.*
import android.widget.ImageView


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
    lateinit var receiver: ServiceReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_list)
        setSupportActionBar(toolbar)
        toolbar.title = title

        if (picture_detail_container != null) {
            twoPane = true
        }
        setupServiceReceiver()
        if (PicturesContent.ITEMS.size == 0) {
            PicturesContent.loadJson(this, receiver)
        } else {
            setupRecyclerView(picture_list)
        }
    }

    private fun setupServiceReceiver() {
        receiver = ServiceReceiver(Handler())
        // This is where we specify what happens when data is received from the service
        receiver.setReceiver(object : ServiceReceiver.Receiver {
            override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                if (resultCode == Activity.RESULT_OK) {
                    val resultValue = resultData.getByteArray("RESULT_VALUE")
                    PicturesContent.parse(resultValue)
                    setupRecyclerView(picture_list)
                }
            }
        })
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
            holder.bind(values[position])
            with(holder.itemView) {
                tag = values[position]
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private var receiver: ServiceReceiver = ServiceReceiver(Handler())

            init {
                // This is where we specify what happens when data is received from the service
                receiver.setReceiver(object : ServiceReceiver.Receiver {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                        if (resultCode == Activity.RESULT_OK) {
                            val resultValue = resultData.getByteArray("RESULT_VALUE")
                            imageView.setImageBitmap(
                                BitmapFactory.decodeByteArray(resultValue, 0, resultValue.size)
                            )
                            val filename = resultData.getString("FILENAME")
                            parentActivity.openFileOutput(filename, Context.MODE_PRIVATE).use {
                                it?.write(resultValue)
                            }
                        }
                    }
                })
            }

            fun bind(item: PicturesContent.PictureItem) {
                contentView.text = item.description
                if (!Finder.setImageIntoView(parentActivity, item.preview, imageView)) {
                    Loader.load(parentActivity, item.preview, receiver)
                }
            }

            val contentView: TextView = view.content
            val imageView: ImageView = view.pictureImageView
        }
    }
}
