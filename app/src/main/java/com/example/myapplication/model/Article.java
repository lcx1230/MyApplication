package com.example.myapplication.model;

public class Article {
    private int articleId;
    private String title;
    private String content;
    private String authorName;  // 修改为 authorName 类型为 String
    private int likeCount;
    private int commentCount;

    public Article() {}

    public Article(int articleId, String title, String content, String authorName, int likeCount, int commentCount) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.authorName = authorName;  // 更新构造函数
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {  // 修改为返回 authorName
        return authorName;
    }

    public void setAuthorName(String authorName) {  // 修改为设置 authorName
        this.authorName = authorName;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
