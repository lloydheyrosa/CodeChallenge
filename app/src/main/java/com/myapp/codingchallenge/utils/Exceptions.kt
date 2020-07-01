package com.myapp.codingchallenge.utils

import java.io.IOException

/**
 * @throws ApiRequestException in every api request call.
 * @throws NoInternetException when an api call detects no internet connection
 * @exception IOException for both of these Exception handlers.
 */
class ApiRequestException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)