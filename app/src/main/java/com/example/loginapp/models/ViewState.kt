package com.example.loginapp.models

import java.lang.Exception

sealed class ViewState<T> {

    class Loading<T> : ViewState<T>()
    class Success<T>(val data: T) : ViewState<T>()
    class Failure<T>(val exception: Exception) : ViewState<T>()

}