package com.myapp.codingchallenge.ui.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.codingchallenge.data.repositories.ItunesItemRepository

/**
 * A ViewModel Factory class for Itunes item list that is initialized so that it can be used for a Viewomodel class if that class has an arguments required.
 * @param repository A Repository class of Itunes item list that is connected to its ViewModel
 */

@Suppress("UNCHECKED_CAST")
class ItunesItemViewModelFactory(
    private val repository: ItunesItemRepository
) :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItunesItemViewModel(repository) as T
    }
}