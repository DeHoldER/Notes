package ru.geekbrains.notes

import java.util.ArrayList

class Publisher {
    private val observers: MutableList<Observer>
    fun subscribe(observer: Observer) {
        observers.add(observer)
    }

    fun unsubscribe(observer: Observer) {
        observers.remove(observer)
    }

    fun notifySingle(note: Note?) {
        for (observer in observers) {
            observer.updateNoteData(note)
            unsubscribe(observer)
        }
    }

    init {
        observers = ArrayList()
    }
}