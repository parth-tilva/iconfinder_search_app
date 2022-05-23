package com.example.assignmentkakcho.util

sealed class Resource<T>(val data:T?, val errorMessage: String?) {
    class Success<T>(data:T): Resource<T>(data,null)
    class Failure<T>(errorMessage: String, data: T?): Resource<T>(data,errorMessage)
    class Loading<T> : Resource<T>(null,null)
}