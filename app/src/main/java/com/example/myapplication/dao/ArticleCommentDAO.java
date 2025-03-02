package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.model.ArticleComment;

import java.util.ArrayList;
import java.util.List;

public class ArticleCommentDAO {
    private DatabaseHelper dbHelper;

    public ArticleCommentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 插入文章评论（Create）
    public long insertArticleComment(ArticleComment comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", comment.getUserId());
        values.put("article_id", comment.getArticleId());
        values.put("comment", comment.getComment());
        long id = db.insert("article_comments", null, values);
        db.close();
        return id;
    }

    // 查询所有文章评论（Retrieve）
    public List<ArticleComment> getAllArticleComments() {
        List<ArticleComment> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM article_comments", null);
        if (cursor.moveToFirst()) {
            do {
                ArticleComment comment = new ArticleComment();
                comment.setCommentId(cursor.getInt(0));
                comment.setUserId(cursor.getInt(1));
                comment.setArticleId(cursor.getInt(2));
                comment.setComment(cursor.getString(3));
                list.add(comment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // 根据ID查询文章评论
    public ArticleComment getArticleCommentById(int commentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM article_comments WHERE comment_id=?", new String[]{String.valueOf(commentId)});
        ArticleComment comment = null;
        if (cursor.moveToFirst()) {
            comment = new ArticleComment();
            comment.setCommentId(cursor.getInt(0));
            comment.setUserId(cursor.getInt(1));
            comment.setArticleId(cursor.getInt(2));
            comment.setComment(cursor.getString(3));
        }
        cursor.close();
        db.close();
        return comment;
    }

    // 更新文章评论（Update）
    public int updateArticleComment(ArticleComment comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", comment.getUserId());
        values.put("article_id", comment.getArticleId());
        values.put("comment", comment.getComment());
        int rowsAffected = db.update("article_comments", values, "comment_id=?", new String[]{String.valueOf(comment.getCommentId())});
        db.close();
        return rowsAffected;
    }

    // 删除文章评论（Delete）
    public void deleteArticleComment(int commentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("article_comments", "comment_id=?", new String[]{String.valueOf(commentId)});
        db.close();
    }
}
