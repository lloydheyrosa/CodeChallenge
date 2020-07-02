package com.myapp.codingchallenge.ui.master

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myapp.codingchallenge.data.db.entities.ItunesItem
import com.myapp.codingchallenge.data.network.apiResponses.ItunesItemsResponse
import com.myapp.codingchallenge.data.repositories.ItunesItemRepository
import com.myapp.codingchallenge.utils.*

/**
 * A ViewModel class of Item list
 * @param repository A repository class of Itunes Item lsit
 */
class ItunesItemViewModel(
    private val repository: ItunesItemRepository
) : ViewModel() {

    var statusListener: StatusListener? = null

    val searchParams = HashMap<String, String>()
    val savedTrackId by lazy {
        repository.getTrackIdSavedFromPrefs()
    }

    /** LiveData for handling data updates and displaying it to the user. */
    val itemsLiveData: LiveData<List<ItunesItem>>
        get() = repository.getSavedItems()

    /**
     * this is the main function in getting items from the api and saving it in the local database,
     * it has a paramter of 'isRefreshRecords' of type Boolean to know if items table will be deleted, I used this for filtering items by term or media type.
    */
    fun getAllItems(isRefreshRecords: Boolean) = Coroutines.main {
        try {
            if(isRefreshRecords)
                deleteAllItems()

            val response = repository.getAllItems(searchParams)
            if(response?.resultCount ?: 0 > 0) {
                statusListener?.onSuccess()
            }
            else statusListener?.onNoResultsFound("No results found. Please try again.")

        } catch (e: NoInternetException) {
            statusListener?.onFailedResponse(e.message ?: "")
        } catch (e: ApiRequestException) {
            statusListener?.onFailedResponse(e.message ?: "")
        }
    }

    /** Get item selected using trackId and returns the item's details */
    suspend fun getItemSelected(strTrackId: String) = repository.getSelectedItem(strTrackId)

    /** Delete all recorded items in the local database */
    private suspend fun deleteAllItems() = repository.deleteAllItems()

    /** Save state if user is in details page */
    fun saveStateInDetailsPage(isSave: Boolean) = repository.saveStateAtDetailsPage(isSave)

    /** Get checker if a user was in details page previously */
    fun isPreviouslyAtDetailsPage() = repository.isPreviouslyAtDetailsPage()

    /** Saves trackId for later use */
    fun saveTrackId(strTrackId: String) = repository.saveTrackIdToPrefs(strTrackId)

    /** Saves date visited */
    fun saveDateVisitedFirst(strDate: String) = repository.saveDateVisitedValue(strDate)

    /** Get previously visited date */
    fun getPreviouslyVisitedDate() = repository.getPreviouslyVisitedDateValue()
}