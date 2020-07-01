package com.myapp.codingchallenge.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.myapp.codingchallenge.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * This class serves as an interceptor for every api request that checks the internet connection if it is available or not.
 *
 * @param context A Context used to instantiate this class.
 * @property appContext An application context from context passed in constructor.
 */
class NetworkConnectionInterceptor(context: Context) : Interceptor {

    private val appContext = context.applicationContext

    /**
     * A function to check if the device has internet connection.
     * @throws NoInternetException if No internet connection detected.
     * @return Response of any type if
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        if(hasInternetConnection())
            return chain.proceed(chain.request())
        else throw NoInternetException("No internet connection. Please try again")
    }

    /**
     * A function to check internet connection
     * @return Boolean value if there's a connection or not
     */
    private fun hasInternetConnection(): Boolean {
        var result = false
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        return result
    }

}