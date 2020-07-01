package com.myapp.codingchallenge.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.myapp.codingchallenge.R
import com.myapp.codingchallenge.ui.master.ItunesItemViewModel
import com.myapp.codingchallenge.ui.master.ItunesItemViewModelFactory
import com.myapp.codingchallenge.utils.Coroutines
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ItemDetailFragment : Fragment(), KodeinAware {

    // implement first kodein for dependency injection, since I am using a ItunesItemViewModelFactory which requires a dependent class
    override val kodein by kodein()

    // this factory was instantiated by the instance of an application since we are using the KodeinAware for dependency injection
    private val factory: ItunesItemViewModelFactory by instance()

    private lateinit var viewModel: ItunesItemViewModel
    private var tvwTrackName: TextView? = null
    private var tvwGenre: TextView? = null
    private var tvwLongDesc: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val trackId = it.getString(ARG_ITEM_ID)
                viewModel = ViewModelProvider(this, factory).get(ItunesItemViewModel::class.java)
                viewModel.saveStateInDetailsPage(true)
                getSelecteditem(trackId ?: "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.item_detail, container, false)
        tvwTrackName = v.findViewById(R.id.tvwTrackName) as TextView
        tvwGenre = v.findViewById(R.id.tvwGenre) as TextView
        tvwLongDesc = v.findViewById(R.id.tvwLongDesc) as TextView
        return v
    }

    private fun getSelecteditem(strTrackid: String) = Coroutines.main {
        viewModel.getItemSelected(strTrackid).observe(viewLifecycleOwner, Observer {
            if(it != null) {
                viewModel.saveTrackId(strTrackid)
                tvwTrackName?.text = it.trackCensoredName ?: ""
                tvwGenre?.text = it.primaryGenreName ?: ""
                tvwLongDesc?.text = it.longDescription ?: "No Description"
            }
        })
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
        const val ARG_ITEM_TRACK_NAME = "item_name"
        const val ARG_ITEM_IMAGE = "item_image_artwork"
    }
}
