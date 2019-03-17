package com.example.lenovo.improvedsearcher

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_favourites.*

class FavouritesActivity : AppCompatActivity(), FavouritesListFragment.OnItemSelected {
    var currentFragment: Fragment? = null

    override fun onItemSelected(id: String) {
        val fragment = PictureDetailFragment().apply {
            arguments = Bundle().apply {
                putString(PictureDetailFragment.ARG_ITEM_ID, id)
            }
        }
        currentFragment = fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.favourites_container, fragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            loadFavList()
        }
    }

    fun loadFavList() {
        val fragment = FavouritesListFragment().apply {
            arguments = Bundle().apply {
                putString(
                    PictureDetailFragment.ARG_ITEM_ID,
                    intent.getStringExtra(PictureDetailFragment.ARG_ITEM_ID)
                )
            }
        }
        currentFragment = fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.favourites_container, fragment)
            .commit()
    }


    override fun onBackPressed() {
        if (currentFragment is PictureDetailFragment) {
            loadFavList()
        } else {
            super.onBackPressed()
        }
    }
}