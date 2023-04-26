package com.example.credential_manager.core

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET
    @JvmSuppressWildcards
    suspend fun getRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, Any>?,
        @QueryMap param: Map<String, Any>?
    ): Response<JsonElement>

    @POST
    @JvmSuppressWildcards
    suspend fun postRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, Any>?,
        @Body body: Map<String, Any>?
    ): Response<JsonElement>

    @PUT
    @JvmSuppressWildcards
    suspend fun putRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, Any>?,
        @Body body: Map<String, Any>?
    ): Response<JsonElement>

    @DELETE
    @JvmSuppressWildcards
    suspend fun deleteRequest(
        @Url api: String,
        @HeaderMap headers: Map<String, Any>?,
        @QueryMap param: Map<String, Any>?
    ): Response<JsonElement>

    @Multipart
    @POST
    @JvmSuppressWildcards
    suspend fun postMultiPart(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @PartMap body: Map<String, RequestBody?>,
        @Part fileMultiPart: MultipartBody.Part?
    ): Response<JsonElement>


    @Multipart
    @PUT
    @JvmSuppressWildcards
    suspend fun updateMultiPart(
        @Url api: String,
        @HeaderMap headers: Map<String, String>?,
        @PartMap body: Map<String, RequestBody?>,
        @Part fileMultiPart: MultipartBody.Part?
    ): Response<JsonElement>

}