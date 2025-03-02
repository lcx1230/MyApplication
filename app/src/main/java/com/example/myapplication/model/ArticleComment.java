package com.example.myapplication.model;

public class ArticleComment {
    private int commentId;
    private int userId;
    private int articleId;
    private String comment;

    public ArticleComment() {}

    public ArticleComment(int commentId, int userId, int articleId, String comment) {
        this.commentId = commentId;
        this.userId = userId;
        this.articleId = articleId;
        this.comment = comment;
    }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getArticleId() { return articleId; }
    public void setArticleId(int articleId) { this.articleId = articleId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
