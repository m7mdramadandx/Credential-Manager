package com.example.credential_manager.todo

import com.example.credential_manager.core.BaseRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepo @Inject constructor() : BaseRepo() {

    suspend fun getTodos() = withContext(Dispatchers.IO) {
        networkManager.getRequest(
            api = "api/Todos/GetTodos",
            parseClass = Array<Todo>::class.java
        )
    }

    suspend fun addTodo(todo: Todo) = withContext(Dispatchers.IO) {
        networkManager.postRequest(
            api = "api/Todos/PostTodo",
            body = todo.toHashMap(),
            parseClass = Boolean::class.java
        )
    }

    suspend fun deleteTodo(id: Int) = withContext(Dispatchers.IO) {
        networkManager.deleteRequest(
            api = "api/Todos/DeleteTodo/$id",
            parseClass = Boolean::class.java
        )
    }
}
