package com.myapp.codingchallenge.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myapp.codingchallenge.data.db.AppDatabase
import com.myapp.codingchallenge.data.db.entities.ItunesItem
import com.myapp.codingchallenge.data.network.ApiInterface
import com.myapp.codingchallenge.data.network.SafeApiRequest
import com.myapp.codingchallenge.data.network.apiResponses.ItunesItemsResponse
import com.myapp.codingchallenge.data.preferences.PreferenceProvider
import com.myapp.codingchallenge.utils.ApiRequestException
import com.myapp.codingchallenge.utils.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * A Repository class of Item List Activity
 * @param api used to call api request
 * @param db used to save and get local item records
 * @param preference used to store and retrieve preference data
 */
class ItunesItemRepository(
    private val api: ApiInterface,
    private val db: AppDatabase,
    private val preference: PreferenceProvider
) : SafeApiRequest() {

    // get items from api
    suspend fun getAllItems(params: Map<String, String>): ItunesItemsResponse? {
        val response = callApiRequest { api.getItems(params) }
        saveItems(response?.results ?: arrayListOf())
        return response
    }

    // get items from local database
    fun getSavedItems(): LiveData<List<ItunesItem>> {
        return db.getItemsDao().getAllItunesItems()
    }

    // delete all items from local database
    suspend fun deleteAllItems(){
        return db.getItemsDao().deleteAllItems()
    }

    // a suspendable function for saving items locally using room database
    private suspend fun saveItems(items: List<ItunesItem>) {
        db.getItemsDao().saveAllItunesItems(items)
    }

    // get item selected from room database
    suspend fun getSelectedItem(trackId: String): LiveData<ItunesItem> {
        return withContext(Dispatchers.IO) {
            db.getItemsDao().getItemByTrackId(trackId)
        }
    }

    // save if user is on item's details page
    fun saveStateAtDetailsPage(isAtDetails: Boolean) {
        preference.saveAtItemDetailsPage(isAtDetails)
    }

    // check if user was previously on item's details page
    fun isPreviouslyAtDetailsPage(): Boolean {
        return preference.isPreviouslyAtDetailsPage()
    }

    // save trackId
    fun saveTrackIdToPrefs(trackId: String) {
        preference.saveTrackId(trackId)
    }

    // get trackId from preferences
    fun getTrackIdSavedFromPrefs(): String {
        return preference.getTrackIdFromPrefs()
    }

    // get date formatted value of previously visited date
    fun getPreviouslyVisitedDateValue(): String {
        return preference.getPreviouslyVisitedDate()
    }

    // save date formatted
    fun saveDateVisitedValue(strDateFormatted: String) {
        preference.saveLastVisitDate(strDateFormatted)
    }
}