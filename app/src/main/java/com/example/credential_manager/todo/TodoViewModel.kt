package com.example.credential_manager.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.credential_manager.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepo: TodoRepo
) : BaseViewModel() {

    fun getTodos(): LiveData<List<Todo>> {
        val mutableLiveData = MutableLiveData<List<Todo>>()
        viewModelScope.launchCatching(
            block = {
                delay(3000)
                val response = todoRepo.getTodos()
                mutableLiveData.postValue(response.toList().sortedBy { it.description })
            },
            onError = {

            }
        )

        return mutableLiveData
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launchCatching(
            block = {
                val response = todoRepo.addTodo(todo)
            },
            onError = {

            }
        )
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launchCatching(
            block = {
                val response = todoRepo.deleteTodo(id)
            },
            onError = {

            }
        )
    }
}