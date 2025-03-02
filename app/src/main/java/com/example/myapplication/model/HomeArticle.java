package com.example.myapplication.model;

public class HomeArticle {
    private String title;
    private String link;
    private String imageUrl;
    private String description;
    private String author;
    private String date;
    private String category;

    public HomeArticle(String title, String link, String imageUrl, String description, String author, String date, String category) {
        this.title = title;
        this.link = link;
        this.imageUrl = imageUrl;
        this.description = description;
        this.author = author;
        this.date = date;
        this.category = category;
    }

    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
}
