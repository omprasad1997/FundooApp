package com.example.loginapp.models

interface CallBack<T> {
    fun onSuccess(data: T)
    fun onFailure(exception: Exception)
}