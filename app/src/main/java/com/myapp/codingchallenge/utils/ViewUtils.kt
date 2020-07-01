package com.myapp.codingchallenge.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Collection of extension functions related to Views
 */
fun View.showSnackBar(message: String, length: Int): Snackbar {
    val sbar = Snackbar.make(this, message, length)
    sbar.show()
    return sbar
}

/** Converts double values to price formatted string */
fun Double.toPriceFormat(): String {
    val nf = NumberFormat.getNumberInstance(Locale.ENGLISH)
    val priceFormat = nf as DecimalFormat
    return priceFormat.format(this)
}