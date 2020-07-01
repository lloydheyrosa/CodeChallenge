package com.myapp.codingchallenge.data.network

import com.myapp.codingchallenge.utils.ApiRequestException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

/**
 * An abstract class that can be used in executing
 * api request with the handling of errors during execution.
 *
 */
abstract class SafeApiRequest {

    /**
     * A lambda generic function that is taking a suspendable function to execute an api request with error handling.
     * @return Returns a Response of any type
     * @throws ApiRequestException if some error occurs during execution.
     */
    suspend fun <T: Any> callApiRequest(call: suspend () -> Response<T?>) : T? {
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()
        }else{
            val error = response.errorBody()?.toString()
            val message = StringBuilder()
            error?.let{
                try{
                    message.append(JSONObject(it).getString("message"))
                }catch(e: JSONException){ }

                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")
            throw ApiRequestException(message.toString())
        }
    }

}