package com.example.myapplication.model;

public class Article {
    private int articleId;
    private String title;
    private String content;
    private int authorId;
    private int likeCount;
    private int commentCount;

    public Article() {}

    public Article(int articleId, String title, String content, int authorId, int likeCount, int commentCount) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public int getArticleId() { return articleId; }
    public void setArticleId(int articleId) { this.articleId = articleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}
