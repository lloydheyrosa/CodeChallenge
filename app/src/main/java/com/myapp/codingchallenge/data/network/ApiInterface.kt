package com.myapp.codingchallenge.data.network

import com.myapp.codingchallenge.data.network.apiResponses.ItunesItemsResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 *
 * An interface for retrofit api calls, this is a default way of the implementation of Retrofit
 *
 */
interface ApiInterface {

    /**
     * @return Returns a Response<ItunesItemsReponse>
     */
    @GET("search")
    suspend fun getItems(@QueryMap queryParams: Map<String, String>): Response<ItunesItemsResponse?>

    companion object {
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterface {

            val baseUrl = "https://itunes.apple.com/"

            // Used to build a network interceptor
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            // Initializing Retrofit
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }

}