package com.example.credential_manager.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class NetworkManager @Inject constructor() {

    @Inject
    lateinit var retrofit: Retrofit

    private val headers: MutableMap<String, String> =
        object : HashMap<String, String>() {
            init {
                put("Accept", "application/json")
                put("Accept-Language", "ar")
            }
        }

    private fun updateHeaders() {
    }

    suspend fun <T> getRequest(
        api: String,
        param: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): T {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                retrofit.create(
                    APIService::class.java
                ).getRequest(api, headers, param), parseClass
            )
        }
    }

    suspend fun <T> postRequest(
        api: String,
        body: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): T {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                retrofit.create(
                    APIService::class.java
                )
                    .postRequest(api, headers, body), parseClass
            )
        }
    }

    suspend fun <T> putRequest(
        api: String,
        body: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): T {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                retrofit.create(
                    APIService::class.java
                )
                    .putRequest(api, headers, body), parseClass
            )
        }
    }

    suspend fun <T> deleteRequest(
        api: String,
        param: Map<String, Any> = HashMap(),
        parseClass: Class<T>,
    ): T {
        updateHeaders()
        return withContext(Dispatchers.IO) {
            parseResponse(
                retrofit.create(
                    APIService::class.java
                )
                    .deleteRequest(api, headers, param), parseClass
            )
        }
    }


    @Throws(
        ErrorResponse::class,
        IOException::class,
        InstantiationException::class,
        IllegalAccessException::class,
        JSONException::class
    )
    private fun <T> parseResponse(
        response: Response<JsonElement>,
        parseClass: Class<T>,
    ): T {
        return try {
            val gson = GsonBuilder().serializeNulls().create()
            if (!response.isSuccessful) {
                var errorResponse = ErrorResponse()
                response.errorBody()?.let {
                    try {
                        val obj = JSONObject(response.errorBody()!!.string())
                        try {
                            val temp = Gson().fromJson(obj.toString(), ErrorResponse::class.java)
                            temp?.let { errorResponse = it }
                        } catch (e: IllegalStateException) {
                        }
                    } catch (ex: JSONException) {
                    }
                }
                errorResponse.code = response.code()
                errorResponse.name = response.message()
                throw errorResponse
            } else {
                gson.fromJson(response.body(), parseClass)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}