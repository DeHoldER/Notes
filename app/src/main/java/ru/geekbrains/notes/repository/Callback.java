package ru.geekbrains.notes.repository;

public interface Callback<T> {

    void onSuccess(T value);

    void onError(Throwable error);
}
