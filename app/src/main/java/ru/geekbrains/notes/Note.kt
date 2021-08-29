package ru.geekbrains.notes

import android.os.Parcelable
import android.os.Parcel
import java.util.*

class Note : Parcelable {
    var title: String
    var text: String
    var id: String
    var date: Date
    var color: Int

    constructor(id: String, title: String, text: String) {
        this.id = id
        this.title = title
        this.text = text
        date = Date()
        color = 0
    }

    constructor(id: String, title: String, text: String, color: Int) {
        this.id = id
        this.title = title
        this.text = text
        date = Date()
        this.color = color
    }

    constructor(id: String, title: String, text: String, color: Int, date: Date) {
        this.id = id
        this.title = title
        this.text = text
        this.date = date
        this.color = color
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString().toString()
        text = `in`.readString().toString()
        id = `in`.readString().toString()
        color = `in`.readInt()
        date = (`in`.readSerializable() as Date?)!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(text)
        dest.writeString(id)
        dest.writeInt(color)
        dest.writeSerializable(date)
    }

    val note: Note
        get() = this

    companion object {
        const val COLOR_WHITE = 0
        const val COLOR_GREEN = 1
        const val COLOR_RED = 2
        const val COLOR_BLUE = 3
        const val COLOR_YELLOW = 4
        const val COLOR_PURPLE = 5
        @JvmField
        val CREATOR: Parcelable.Creator<Note?> = object : Parcelable.Creator<Note?> {
            override fun createFromParcel(`in`: Parcel): Note? {
                return Note(`in`)
            }

            override fun newArray(size: Int): Array<Note?> {
                return arrayOfNulls(size)
            }
        }
    }
}