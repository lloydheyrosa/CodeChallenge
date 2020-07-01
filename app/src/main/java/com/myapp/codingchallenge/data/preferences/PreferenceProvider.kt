package com.myapp.codingchallenge.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_IS_IN_DETAILS_PAGE = "is_in_details_page"
private const val KEY_PREVIOUSLY_VISITED = "key_previous_visit"
private const val KEY_TRACK_ID = "key_track_id"
private const val KEY_FILTER_TERMS = "key_filter_terms"
private const val KEY_FILTER_MEDIA = "key_filter_media"

/**
 * A Class used for storing or retrieving SharedPreferences data.
 *
 * @param mContext A context of an application.
 * @property context An application context.
 * @property preference A sharedpreference handler
 */
class PreferenceProvider(mContext: Context) {
    private val context = mContext.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Saving last date visit
     * @param savedAt A date formatted in String
     */
    fun saveLastVisitDate(savedAt: String) {
        preference.edit().putString(
            KEY_PREVIOUSLY_VISITED,
            savedAt
        ).apply()
    }

    /**
     * Saving state if user was on details page
     * @param isAtDetailsPage If user was in details page after exiting the app
     */
    fun saveAtItemDetailsPage(isAtDetailsPage: Boolean) {
        preference.edit().putBoolean(
            KEY_IS_IN_DETAILS_PAGE,
            isAtDetailsPage
        ).apply()
    }

    /**
     * Saving trackId to render item details
     * @param trackId track id of an item to be saved
     */
    fun saveTrackId(trackId: String) {
        preference.edit().putString(
            KEY_TRACK_ID,
            trackId
        ).apply()
    }

    /**
     * Getting previously visitted date
     * @return A string formatted date of last visit
     */
    fun getPreviouslyVisitedDate(): String {
        return preference.getString(KEY_PREVIOUSLY_VISITED, "") ?: ""
    }

    /**
     * Getting state if user was on details page
     * @return Boolean value to check in displaying first the details or not
     */
    fun isPreviouslyAtDetailsPage(): Boolean {
        return preference.getBoolean(KEY_IS_IN_DETAILS_PAGE, false)
    }

    /**
     * @return trackId for checking its details and display item details
     */
    fun getTrackIdFromPrefs(): String {
        return preference.getString(KEY_TRACK_ID, "") ?: ""
    }

    /**
     * @return term of type String that is used for filtering items and params for api request of getting items
     */
    fun getSavedTerms(): String {
        return preference.getString(KEY_FILTER_TERMS, "star") ?: "star"
    }

    /**
     * @return saved media used for filtering items and params for api request of getting items
     */
    fun getSavedMediaType(): String {
        return preference.getString(KEY_FILTER_MEDIA, "") ?: ""
    }

    /**
     * Saves a term selected in filtering
     * @param term as selected term
     */
    fun saveTerms(term: String) {
        preference.edit().putString(
            KEY_FILTER_TERMS,
            term
        ).apply()
    }

    /**
     * Saves a media selected in filtering
     * @param media as selected media
     */
    fun saveMedia(media: String) {
        preference.edit().putString(
            KEY_FILTER_MEDIA,
            media
        ).apply()
    }
}