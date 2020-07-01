package com.myapp.codingchallenge.data.repositories

import com.google.gson.Gson
import com.myapp.codingchallenge.data.preferences.PreferenceProvider
import com.myapp.codingchallenge.ui.filter.ItunesMediaType

/**
 * A Repository class of Filter activity
 * @param preference used to store and retrieve preference data
 * @param gson used to convert selected media object to json string.
 */

class FilterRepository(
    private val preference: PreferenceProvider,
    private val gson: Gson
) {

    // get saved terms
    fun getTerms(): String {
        return preference.getSavedTerms()
    }

    // save terms
    fun saveTerms(strTerm: String) {
        preference.saveTerms(strTerm)
    }

    // get saved media
    fun getMediaType(): ItunesMediaType {
        val strMedia = preference.getSavedMediaType()
        return if(strMedia.isNotEmpty()) gson.fromJson(strMedia, ItunesMediaType::class.java) else ItunesMediaType("Movie", "movie")
    }

    // save media type
    fun saveMediaType(strMedia: String) {
        preference.saveMedia(strMedia)
    }
}