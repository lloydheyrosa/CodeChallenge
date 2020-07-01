package com.myapp.codingchallenge.ui.filter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.myapp.codingchallenge.R
import com.myapp.codingchallenge.ui.master.ItunesItemViewModel
import com.myapp.codingchallenge.ui.master.ItunesItemViewModelFactory
import kotlinx.android.synthetic.main.activity_filter.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class FilterActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory : FilterViewModelFactory by instance()
    lateinit var viewModel: FilterViewModel
    private val mediaList: ArrayList<ItunesMediaType> = ArrayList()
    private val gson : Gson by instance()

    private var selectedMedia: ItunesMediaType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        viewModel = ViewModelProvider(this, factory).get(FilterViewModel::class.java)
        setSupportActionBar(toolbarFilter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initUI()
    }

    /**
     * Initialize UI components
     */
    private fun initUI() {
        txtTerm.setText(viewModel.getTerm())
        selectedMedia = viewModel.getSelectedMediaType()
        txtMediaType.setText(selectedMedia?.toString())

        // get all label and values of media in array resource
        val arrMediaLabel = resources.getStringArray(R.array.media_labels)
        val arrMediaValues = resources.getStringArray(R.array.media_values)

        // store media type object in Arraylist
        for (i in arrMediaValues.indices) {
            val newMedia = ItunesMediaType(arrMediaLabel[i], arrMediaValues[i])
            mediaList.add(newMedia)
        }

        val mediaAdapter: ArrayAdapter<ItunesMediaType> = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, mediaList)
        txtMediaType.setAdapter(mediaAdapter)
        txtMediaType.setOnItemClickListener { v, _, position, _ ->
            selectedMedia = v.adapter.getItem(position) as ItunesMediaType
            txtMediaType.setText(selectedMedia?.toString())
        }
        txtMediaType.addTextChangedListener {
            tilMediaType.error = null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_filter -> {
                val isValidMedia = mediaList.find { it.toString() == txtMediaType.text.toString() } != null
                if(isValidMedia) {
                    saveFilters()
                    val intent = Intent().apply {
                        putExtra(KEY_TERM, txtTerm.text.toString())
                        putExtra(KEY_MEDIA, selectedMedia?.paramValue)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    onBackPressed()
                }
                else tilMediaType.error = getString(R.string.invalid_mediatype)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * save term and media selected for filtering
     */
    private fun saveFilters() {
        viewModel.saveTerm(txtTerm.text.toString())
        viewModel.saveMediaType(gson.toJson(selectedMedia))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val REQUEST_FILTER_ITEMS = 808
        const val KEY_TERM = "term"
        const val KEY_MEDIA = "media"
    }

}
