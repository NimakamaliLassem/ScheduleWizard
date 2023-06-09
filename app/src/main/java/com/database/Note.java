package com.database;

public class Note {
    private String title;

    private String text;
    private String type;

    public Note(String title, String type) {
        this.title = title;
        this.type = type;
    }


    public Note(String title, String text, String type) {
        this.title = title;
        this.text = text;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
