package com.myapp.codingchallenge.utils

/**
 * An interface to uniformly update activities if getting items are successful, no records or failed.
 */
interface StatusListener {

    fun onSuccess()
    fun onFailedResponse(message: String)
    fun onNoResultsFound(message: String)
}