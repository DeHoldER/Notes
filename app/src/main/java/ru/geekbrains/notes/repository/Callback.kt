package ru.geekbrains.notes.repository

interface Callback<T> {
    fun onSuccess(value: T)
    fun onError(error: Throwable?)
}