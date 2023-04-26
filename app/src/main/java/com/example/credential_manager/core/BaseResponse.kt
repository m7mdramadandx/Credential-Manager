package com.example.credential_manager.core

import com.google.gson.annotations.SerializedName

class BaseResponse<T> {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("errorList")
    val errorList: ArrayList<String> = ArrayList()

    @SerializedName("data")
    var data: T? = null
}