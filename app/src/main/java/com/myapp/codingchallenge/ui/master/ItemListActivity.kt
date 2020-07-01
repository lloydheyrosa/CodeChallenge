package com.myapp.codingchallenge.ui.master

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.myapp.codingchallenge.R
import com.myapp.codingchallenge.data.db.entities.ItunesItem
import com.myapp.codingchallenge.ui.detail.ItemDetailActivity
import com.myapp.codingchallenge.ui.detail.ItemDetailFragment
import com.myapp.codingchallenge.ui.filter.FilterActivity
import com.myapp.codingchallenge.utils.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemListActivity : AppCompatActivity(), StatusListener, KodeinAware {

    override val kodein by kodein()
    private val factory : ItunesItemViewModelFactory by instance()
    private val mLayoutManager: WrapContentLinearLayoutManager by instance()

    private var snackBarLazyLoad: Snackbar? = null

    private var isTwoPane: Boolean = false
    private val itemsList: ArrayList<ItunesItem> = ArrayList()
    private val allItemsForFiltering: ArrayList<ItunesItem> = ArrayList()
    private var limitCount = 0
    private val country = "au"
    private var strTerm = "star"
    private var strMedia = "movie"

    private var isScrolling = false

    lateinit var viewModel: ItunesItemViewModel
    lateinit var listAdapter: ItunesItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        viewModel = ViewModelProvider(this, factory).get(ItunesItemViewModel::class.java)
        viewModel.statusListener = this
        setDatePreviouslyVisited()
        saveDateVisited()

        setSupportActionBar(toolbar)
        supportActionBar?.title = title

        if(viewModel.isPreviouslyAtDetailsPage()) {
            val trackId = viewModel.savedTrackId
            proceedToSelectedItemDetailsPage(trackId)
        }

        if (item_detail_container != null) {
            isTwoPane = true
        }

        listAdapter = ItunesItemListAdapter(this, itemsList, isTwoPane)
        rvwItunesItems?.adapter = listAdapter
        rvwItunesItems?.layoutManager = mLayoutManager

        rvwItunesItems?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val nCurrentItems = mLayoutManager.childCount
                val nTotalItems = mLayoutManager.itemCount
                val nScrollOutItems = mLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && nCurrentItems + nScrollOutItems == nTotalItems) {
                    // fetch data
                    isScrolling = false

                    if (snackBarLazyLoad != null)
                        snackBarLazyLoad?.dismiss()

                    snackBarLazyLoad = activity_item_list.showSnackBar("Loading more. Please wait.", Snackbar.LENGTH_INDEFINITE)

                    addItemsSearchParams()
                    getAllItems(false)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }
        })

        swipeRefreshItems?.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorAccent),
            ContextCompat.getColor(this, R.color.colorPrimaryDark))

        swipeRefreshItems?.setOnRefreshListener {
            refreshItems()
        }

        viewModel.itemsLiveData.observe(this, Observer {
            allItemsForFiltering.clear()
            allItemsForFiltering.addAll(it)
            itemsList.clear()
            itemsList.addAll(it)
            listAdapter.notifyDataSetChanged()
        })

        addItemsSearchParams()
        swipeRefreshItems?.isRefreshing = true
        getAllItems(false)
    }

    /**
     * Refresh item list by deleting local records and displaying new records from api request.
     */
    private fun refreshItems() {
        itemsList.clear()
        allItemsForFiltering.clear()
        swipeRefreshItems?.isRefreshing = true
        limitCount = 0 // restart result count since this is a total refresh of items
        addItemsSearchParams()
        getAllItems(true)
    }

    /**
     * adding parameters for the api request
     */
    private fun addItemsSearchParams() {
        limitCount += 25

        viewModel.searchParams["term"] = strTerm
        viewModel.searchParams["country"] = country
        viewModel.searchParams["media"] = strMedia
        viewModel.searchParams["limit"] = limitCount.toString()
    }

    private fun setDatePreviouslyVisited() {

        val strDate = viewModel.getPreviouslyVisitedDate()
        val strDatePreviouslyVisited = getString(R.string.last_visit, strDate)

        if(strDate.isNotEmpty()) {
            tvwDatePreviouslyVisited?.visibility = View.VISIBLE
            tvwDatePreviouslyVisited?.text = strDatePreviouslyVisited
        }
        else
            tvwDatePreviouslyVisited?.visibility = View.GONE
    }

    private fun saveDateVisited() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        viewModel.saveDateVisitedFirst(dateFormat.format(Date()))
    }

    private fun getAllItems(isRefreshRecords: Boolean) {
        viewModel.getAllItems(isRefreshRecords)
    }

    /**
     * Go to details page
     */
    private fun proceedToSelectedItemDetailsPage(strTrackid: String) = Coroutines.main {
        viewModel.getItemSelected(strTrackid).observeOnce(this, Observer {
            if(it != null) {
                val intent = Intent(this, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, strTrackid)
                    putExtra(ItemDetailFragment.ARG_ITEM_TRACK_NAME, it.trackName)
                    putExtra(ItemDetailFragment.ARG_ITEM_IMAGE, it.artworkUrl100)
                }
                startActivity(intent)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.saveStateInDetailsPage(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                val intentFilter = Intent(this, FilterActivity::class.java)
                startActivityForResult(intentFilter, FilterActivity.REQUEST_FILTER_ITEMS)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    listAdapter.filter(query ?: "", allItemsForFiltering)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    listAdapter.filter(newText ?: "", allItemsForFiltering)
                    return true
                }
            })
            searchView.setOnCloseListener { false }
        }


        return super.onCreateOptionsMenu(menu)
    }

    /**
     * A function that will be executed if api request is successful
     */
    override fun onSuccess() {
        relNoResults.visibility = View.GONE
        rvwItunesItems.visibility = View.VISIBLE
        swipeRefreshItems?.isRefreshing = false
        snackBarLazyLoad?.dismiss()
    }

    /**
     * A function that will be executed if api request failed
     */
    override fun onFailedResponse(message: String) {
        swipeRefreshItems?.isRefreshing = false
        snackBarLazyLoad?.dismiss()
        activity_item_list.showSnackBar(message, Snackbar.LENGTH_SHORT)
    }

    /**
     * A function that will be executed if api request returns no results
     */
    override fun onNoResultsFound(message: String) {
        swipeRefreshItems?.isRefreshing = false
        snackBarLazyLoad?.dismiss()
        tvwNoResultLabel.text = message
        relNoResults.visibility = View.VISIBLE
        rvwItunesItems.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == FilterActivity.REQUEST_FILTER_ITEMS) {
            strTerm = data?.getStringExtra(FilterActivity.KEY_TERM) ?: "star"
            strMedia = data?.getStringExtra(FilterActivity.KEY_MEDIA) ?: "movie"
            refreshItems()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
