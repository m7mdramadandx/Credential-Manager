package com.example.credential_manager.todo

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("id") val id: Int,
    @SerializedName("description") val description: String
)

fun Todo.toHashMap() =
    hashMapOf(
        "id" to id,
        "description" to description
    )
