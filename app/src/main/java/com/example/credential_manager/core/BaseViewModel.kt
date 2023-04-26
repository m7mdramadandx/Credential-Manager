package com.example.credential_manager.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel() {

    val errorLiveData: MutableLiveData<Throwable> by lazy {
        MutableLiveData<Throwable>()
    }

    inline fun CoroutineScope.launchCatching(
        noinline block: suspend CoroutineScope.() -> Unit,
        crossinline onError: (Throwable) -> Unit,
    ) {

        launch(
            CoroutineExceptionHandler { _, throwable ->
                val newThrowable: Throwable = when (throwable) {
                    is HttpException -> Throwable(message = "")
                    is UnknownHostException -> Throwable(message = "غير متصل بالانترنت")
                    is SocketTimeoutException -> Throwable(message = "هناك خطأ ما, حاول مرة اخرى")
                    else -> Throwable(message = "")
                }
                onError(newThrowable)
                errorLiveData.postValue(newThrowable)
            },
            block = block
        )
    }
}
