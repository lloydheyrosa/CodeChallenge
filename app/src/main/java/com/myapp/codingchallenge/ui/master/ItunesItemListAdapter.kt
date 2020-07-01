package com.myapp.codingchallenge.ui.master

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.myapp.codingchallenge.R
import com.myapp.codingchallenge.data.db.entities.ItunesItem
import com.myapp.codingchallenge.ui.detail.ItemDetailActivity
import com.myapp.codingchallenge.ui.detail.ItemDetailFragment
import com.myapp.codingchallenge.utils.toPriceFormat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_content.view.*

/**
 * An Adapter class used to bind views into recyclerview.
 * @param parentActivity A context used to bind views
 * @param itemList A list of items to display
 * @param twoPane A checker if the device has a larger screen, it will display lsit in two pane.
 * @property onClickListener click listener for selecting an itunes item
 */
class ItunesItemListAdapter(private val parentActivity: ItemListActivity,
                            private val itemList: ArrayList<ItunesItem>,
                            private val twoPane: Boolean) :
    RecyclerView.Adapter<ItunesItemListAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {

        onClickListener = View.OnClickListener { v ->
            val item = v.tag as ItunesItem
            if (twoPane) {

                val fragment = ItemDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.trackId)
                        }
                    }

                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()

            } else {

                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.trackId)
                    putExtra(ItemDetailFragment.ARG_ITEM_TRACK_NAME, item.trackName)
                    putExtra(ItemDetailFragment.ARG_ITEM_IMAGE, item.artworkUrl100)
                }

                v.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.tvwTrackName.text = item.trackName
        holder.tvwGenre.text = item.primaryGenreName

        val dPrice = item.trackPrice?.toDoubleOrNull() ?: 0.0
        holder.tvwPrice.text = dPrice.toPriceFormat()

        if(item.currency.equals("AUD", true))
            holder.tvwCurrency.text = "A$"
        else holder.tvwCurrency.text = item.currency

        if(item.artworkUrl100?.isNotEmpty() == true) {
            Picasso.get()
                .load(item.artworkUrl100)
                .error(R.drawable.img_placeholder)
                .into(holder.imgItem)
        }
        else {
            Picasso.get()
                .load(R.drawable.img_placeholder)
                .into(holder.imgItem)
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    /**
     * A function that will filter displayed items in the list by trackName or trackCensoredName and genre
     */
    fun filter(strText: String, allItemList: List<ItunesItem>) {
        val filteredItemsList: ArrayList<ItunesItem> = ArrayList()
        filteredItemsList.addAll(allItemList)

        itemList.clear()

        if(strText.isNotEmpty()) {
            itemList.addAll(
                filteredItemsList.filter { item ->
                    item.trackName?.contains(strText, true) == true ||
                            item.trackCensoredName?.contains(strText, true) == true ||
                            item.primaryGenreName?.contains(strText, true) == true
                }
            )
        }
        else itemList.addAll(allItemList)

        notifyDataSetChanged()
    }

    override fun getItemCount() = itemList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvwTrackName: TextView = view.tvwTrackName
        val tvwGenre: TextView = view.tvwGenre
        val tvwPrice: TextView = view.tvwPrice
        val tvwCurrency: TextView = view.tvwCurrency
        val imgItem: ShapeableImageView = view.imgItem
    }
}