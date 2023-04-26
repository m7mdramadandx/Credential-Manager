package com.example.credential_manager.core

import com.google.gson.annotations.SerializedName

class ErrorModel(
    @SerializedName("Id") val id: Int?,
    @SerializedName("Message") val message: String?,
)