package com.example.loginapp.HelperClasses

interface CallBack<T> {
    fun onSuccess(data: T)
    fun onFailure(exception: Exception)
}