package com.myapp.codingchallenge.ui.filter

/**
 * a class just to determine the itunes media type and their parameter value and used for AutoCompleteTextView
 * @param strGivenName a string label for AutoCompleteTextView when selecting a media.
 * @param strValue a string value that serves as a parameter in getting items via API request
 */
class ItunesMediaType(strGivenName: String, strValue: String) {
    private var name = strGivenName
    val paramValue = strValue

    override fun toString(): String {
        return name
    }
}