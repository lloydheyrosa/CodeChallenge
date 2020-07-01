package com.myapp.codingchallenge.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.codingchallenge.data.repositories.FilterRepository

/**
 * A ViewModel Factory class that is initialized so that it can be used for a Viewomodel class if that class has an arguments required.
 * @param repository A Repository class connected to the ViewModel
 */
@Suppress("UNCHECKED_CAST")
class FilterViewModelFactory(
    private val repository: FilterRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FilterViewModel(repository) as T
    }
}