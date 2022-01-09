package com.example.android.newsapp;

public class News {
    private String title;
    private String information;
    private String date;
    private String url;
    private String authorName;


    public News(String title, String information, String authorName, String time, String url) {
        this.title = title;
        this.information = information;
        this.date = time;
        this.url = url;
        this.authorName = authorName;
    }


    public String getTitle() {
        return this.title;
    }


    public String getInformation() {
        return this.information;
    }


    public String getDate() {
        return this.date;
    }


    public String getUrl() {
        return this.url;
    }


    public String getAuthorName() {
        return this.authorName;
    }

}


