package com.myapp.codingchallenge.data.network.apiResponses

import com.myapp.codingchallenge.data.db.entities.ItunesItem

/**
 * This is a data class of an API response in getting items.
 *
 * @constructor properties of ItunesItemREsponse
 * @property resultCount A count of results
 * @property results A type List<ItunesItems>
 */
data class ItunesItemsResponse(
    val resultCount: Int?,
    val results: List<ItunesItem>?
)