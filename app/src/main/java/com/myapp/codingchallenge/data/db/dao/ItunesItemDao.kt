package com.myapp.codingchallenge.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.myapp.codingchallenge.data.db.entities.ItunesItem

@Dao
interface ItunesItemDao {

    /**
     * Save all items to local database
     * @param List<ItunesItem> for saving list to database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllItunesItems(quotes : List<ItunesItem>)

    /**
     * Getting all items from local database
     * @return a LiveData of type List<ItunesItem>
     */
    @Query("SELECT * FROM ItunesItem")
    fun getAllItunesItems() : LiveData<List<ItunesItem>>

    /**
     * Getting all items from local database
     * @return a LiveData of type List<ItunesItem>
     */
    @Query("SELECT * FROM ItunesItem WHERE trackId = :strTrackId")
    fun getItemByTrackId(strTrackId: String) : LiveData<ItunesItem>

    /**
     * Deleting or truncating the ItunesItem Table from local database
     */
    @Query("DELETE FROM ItunesItem")
    suspend fun deleteAllItems()

}