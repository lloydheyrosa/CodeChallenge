package com.myapp.codingchallenge.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A data class of *ItunesItems*.
 *
 * This class is also a data class that contains all itunes item properties
 *
 * @constructor Creates a data class of ItunesItems with a required parameters as its properties
 * @property trackId as the primary key
 */
@Entity
data class ItunesItem(

    @PrimaryKey(autoGenerate = false)
    val trackId: String,

    val wrapperType: String?,
    val kind: String?,
    val artistId: String?,
    val collectionId: String?,
    val artistName: String?,
    val collectionName: String?,
    val trackName: String?,
    val collectionCensoredName: String?,
    val trackCensoredName: String?,
    val artistViewUrl: String?,
    val collectionViewUrl: String?,
    val trackViewUrl: String?,
    val previewUrl: String?,
    val artworkUrl30: String?,
    val artworkUrl60: String?,
    val artworkUrl100: String?,
    val collectionPrice: String?,
    val trackPrice: String?,
    val releaseDate: String?,
    val collectionExplicitness: String?,
    val trackExplicitness: String?,
    val discCount: String?,
    val discNumber: String?,
    val trackCount: String?,
    val trackNumber: String?,
    val trackTimeMillis: String?,
    val country: String?,
    val currency: String?,
    val primaryGenreName: String?,
    val isStreamable: Boolean,
    val collectionArtistId: String?,
    val collectionArtistViewUrl: String?,
    val trackRentalPrice: String?,
    val collectionHdPrice: String?,
    val trackHdPrice: String?,
    val trackHdRentalPrice: String?,
    val contentAdvisoryRating: String?,
    val shortDescription: String?,
    val longDescription: String?,
    val hasITunesExtras: Boolean
)