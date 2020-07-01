package com.myapp.codingchallenge.ui.filter

import androidx.lifecycle.ViewModel
import com.myapp.codingchallenge.data.repositories.FilterRepository

/**
 * A ViewModel class of Filter page
 * @param repository used to perform functions from the Repository class
 */

class FilterViewModel(
    private val repository: FilterRepository
) : ViewModel() {

    fun getTerm() = repository.getTerms()
    fun getSelectedMediaType() = repository.getMediaType()

    fun saveTerm(strTerm: String) = repository.saveTerms(strTerm)
    fun saveMediaType(strMediaType: String) = repository.saveMediaType(strMediaType)

}