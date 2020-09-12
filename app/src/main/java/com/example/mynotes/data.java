package com.example.mynotes;

public class data {
    String note;
    String title;
    String date;
    public data(String note, String title, String date) {
        this.note = note;
        this.title = title;
        this.date = date;
    }
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
