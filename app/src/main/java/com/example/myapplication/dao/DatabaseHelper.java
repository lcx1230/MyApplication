package com.example.myapplication.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "campus_psychology.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建 users 表（用户表）
        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "gender TEXT, " +
                "phone TEXT, " +
                "email TEXT, " +
                "avatar_url TEXT, " +
                "department TEXT, " +
                "major TEXT, " +
                "title TEXT, " +
                "role TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);

        // 创建 counselors 表（心理咨询师表）
        String CREATE_COUNSELORS_TABLE = "CREATE TABLE counselors (" +
                "counselor_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "gender TEXT NOT NULL, " +
                "qualifications TEXT, " +
                "specialization TEXT, " +
                "avatar_url TEXT, " +
                "certificate_url TEXT, " + // 新增字段：存储资格证书路径
                "available_time TEXT NOT NULL DEFAULT '[]'" + // JSON 格式，初始为空
                ")";
        db.execSQL(CREATE_COUNSELORS_TABLE);


        // 创建 appointments 表（预约表）
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE appointments (" +
                "appointment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "counselor_id INTEGER NOT NULL, " +
                "appointment_time TEXT NOT NULL, " + // JSON 格式存储时间段
                "status TEXT DEFAULT 'pending', " +
                "feedback_id INTEGER, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (counselor_id) REFERENCES counselors(counselor_id), " +
                "FOREIGN KEY (feedback_id) REFERENCES feedback(feedback_id)" +
                ")";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);

        // 创建 feedback 表（用户反馈表）
        String CREATE_FEEDBACK_TABLE = "CREATE TABLE feedback (" +
                "feedback_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "counselor_id INTEGER NOT NULL, " +
                "rating INTEGER CHECK (rating BETWEEN 1 AND 5), " +
                "comment TEXT, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (counselor_id) REFERENCES counselors(counselor_id)" +
                ")";
        db.execSQL(CREATE_FEEDBACK_TABLE);

        // 创建 articles 表（文章表）
        String CREATE_ARTICLES_TABLE = "CREATE TABLE articles (" +
                "article_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT NOT NULL, " +
                "author_id INTEGER NOT NULL, " +
                "like_count INTEGER DEFAULT 0, " +
                "comment_count INTEGER DEFAULT 0, " + // 新增评论数量
                "FOREIGN KEY (author_id) REFERENCES users(user_id)" +
                ")";
        db.execSQL(CREATE_ARTICLES_TABLE);

        // 创建 article_comments 表（文章评论表）
        String CREATE_ARTICLE_COMMENTS_TABLE = "CREATE TABLE article_comments (" +
                "comment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "article_id INTEGER NOT NULL, " +
                "comment TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (article_id) REFERENCES articles(article_id)" +
                ")";
        db.execSQL(CREATE_ARTICLE_COMMENTS_TABLE);

        // 创建 messages 表（消息表）
        String CREATE_MESSAGES_TABLE = "CREATE TABLE messages (" +
                "message_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER NOT NULL, " +
                "appointment_id INTEGER, " +
                "message_text TEXT NOT NULL, " +
                "is_read INTEGER DEFAULT 0, " + // 0 = 未读, 1 = 已读
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)" +
                ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) { // 确保数据库版本增加
            db.execSQL("ALTER TABLE counselors ADD COLUMN certificate_url TEXT");
        }
    }

}
