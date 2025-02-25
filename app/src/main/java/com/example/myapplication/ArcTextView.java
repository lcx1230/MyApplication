package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ArcTextView extends AppCompatTextView {

    private final Paint paint; // 画笔
    private final Path path; // 路径
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    public ArcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 初始化画笔，开启抗锯齿
        path = new Path(); // 初始化路径
        // 初始化画笔，设置文字对齐方式为居中
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String displayedText = getText().toString();
    }
}