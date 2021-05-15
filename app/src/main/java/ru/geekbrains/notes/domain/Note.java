package ru.geekbrains.notes.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Note implements Parcelable {

    private String title;
    private String text;
    private String id;
    private Date date;
    private int color;

    public static final int COLOR_WHITE = 0;
    public static final int COLOR_GREEN = 1;
    public static final int COLOR_RED = 2;
    public static final int COLOR_BLUE = 3;
    public static final int COLOR_YELLOW = 4;
    public static final int COLOR_PURPLE = 5;

    public Note(String id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = new Date();
        this.color = 0;
    }

    public Note(String id, String title, String text, int color) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = new Date();
        this.color = color;
    }

    protected Note(Parcel in) {
        title = in.readString();
        text = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Note getNote() {
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
