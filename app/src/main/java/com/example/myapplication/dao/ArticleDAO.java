package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {
    private DatabaseHelper dbHelper;

    public ArticleDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入文章（Create）
    public long insertArticle(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", article.getTitle());
        values.put("content", article.getContent());
        values.put("author_name", article.getAuthorName());  // 修改为 author_name
        values.put("like_count", article.getLikeCount());
        values.put("comment_count", article.getCommentCount());
        long id = db.insert("articles", null, values);
        db.close();
        return id;
    }

    // 查询所有文章（Retrieve）
    public List<Article> getAllArticles() {
        List<Article> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM articles", null);
        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setArticleId(cursor.getInt(0));
                article.setTitle(cursor.getString(1));
                article.setContent(cursor.getString(2));
                article.setAuthorName(cursor.getString(3));  // 修改为 author_name
                article.setLikeCount(cursor.getInt(4));
                article.setCommentCount(cursor.getInt(5));
                list.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 根据ID查询文章
    public Article getArticleById(int articleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM articles WHERE article_id=?", new String[]{String.valueOf(articleId)});
        Article article = null;
        if (cursor.moveToFirst()) {
            article = new Article();
            article.setArticleId(cursor.getInt(0));
            article.setTitle(cursor.getString(1));
            article.setContent(cursor.getString(2));
            article.setAuthorName(cursor.getString(3));  // 修改为 author_name
            article.setLikeCount(cursor.getInt(4));
            article.setCommentCount(cursor.getInt(5));
        }
        cursor.close();
        db.close();
        return article;
    }

    // 更新文章（Update）
    public int updateArticle(Article article) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", article.getTitle());
        values.put("content", article.getContent());
        values.put("author_name", article.getAuthorName());  // 修改为 author_name
        values.put("like_count", article.getLikeCount());
        values.put("comment_count", article.getCommentCount());
        int rowsAffected = db.update("articles", values, "article_id=?", new String[]{String.valueOf(article.getArticleId())});
        db.close();
        return rowsAffected;
    }

    // 删除文章（Delete）
    public void deleteArticle(int articleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("articles", "article_id=?", new String[]{String.valueOf(articleId)});
        db.close();
    }
    // 获取第一篇文章
    public Article getFirstArticle() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM articles ORDER BY article_id ASC LIMIT 1", null);
        Article article = null;
        if (cursor.moveToFirst()) {
            article = new Article();
            article.setArticleId(cursor.getInt(0));
            article.setTitle(cursor.getString(1));
            article.setContent(cursor.getString(2));
            article.setAuthorName(cursor.getString(3));
            article.setLikeCount(cursor.getInt(4));
            article.setCommentCount(cursor.getInt(5));
        }
        cursor.close();
        db.close();
        return article;
    }

    // 更新点赞数
    public void updateLikeCount(int articleId, int newLikeCount) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("like_count", newLikeCount);
        db.update("articles", values, "article_id=?", new String[]{String.valueOf(articleId)});
        db.close();
    }

}
