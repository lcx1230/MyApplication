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
    // 查询某篇文章的所有评论
    public List<ArticleComment> getCommentsByArticleId(int articleId) {
        List<ArticleComment> commentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "article_comments", // 表名
                null, // 查询所有列
                "article_id = ?", // WHERE 条件
                new String[]{String.valueOf(articleId)}, // WHERE 参数
                null, // groupBy
                null, // having
                "comment_id ASC" // 改为按 comment_id 排序（或其他你希望的字段）
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                ArticleComment comment = new ArticleComment();
                comment.setCommentId(cursor.getInt(cursor.getColumnIndexOrThrow("comment_id")));
                comment.setArticleId(cursor.getInt(cursor.getColumnIndexOrThrow("article_id")));
                comment.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                comment.setComment(cursor.getString(cursor.getColumnIndexOrThrow("comment")));
                commentList.add(comment);
            }
            cursor.close();
        }

        return commentList;
    }

    // 获取某篇文章的评论数量
    public int getCommentCountByArticleId(int articleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // 使用 COUNT(*) 查询评论数量
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM article_comments WHERE article_id = ?", new String[]{String.valueOf(articleId)});

        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // 获取第一列的值，即评论数量
            }
            cursor.close();
        }

        db.close();
        return count;
    }

}
