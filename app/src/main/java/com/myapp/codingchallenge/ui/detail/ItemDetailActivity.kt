package com.myapp.codingchallenge.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.myapp.codingchallenge.R
import com.myapp.codingchallenge.data.db.entities.ItunesItem
import com.myapp.codingchallenge.ui.master.ItemListActivity
import com.myapp.codingchallenge.ui.master.ItunesItemViewModel
import com.myapp.codingchallenge.ui.master.ItunesItemViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_list_content.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(ItemDetailFragment.ARG_ITEM_TRACK_NAME)

        Picasso.get()
            .load(intent.getStringExtra(ItemDetailFragment.ARG_ITEM_IMAGE))
            .into(imgItemArtwork)

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = ItemDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(
                            ItemDetailFragment.ARG_ITEM_ID,
                            intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
                        )
                    }
                }

            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
