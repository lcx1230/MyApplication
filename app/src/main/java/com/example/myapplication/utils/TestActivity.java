package com.example.myapplication.utils;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HomeArticleAdapter;
import com.example.myapplication.model.HomeArticle;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "爬取文章";
    private RecyclerView recyclerView;
    private HomeArticleAdapter articleAdapter;
    private List<HomeArticle> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleAdapter = new HomeArticleAdapter(this, articleList);
        recyclerView.setAdapter(articleAdapter);

        // 开启子线程爬取数据
        new Thread(() -> {
            int maxPages = 10; // 设定爬取页数
            for (int i = 1; i <= maxPages; i++) {
                String url = "https://www.xinli001.com/info?page=" + i;
                Log.d(TAG, "正在爬取页面: " + url);

                List<WebScraper.Article> articles = WebScraper.getArticles(url);
                for (WebScraper.Article article : articles) {
                    Log.d(TAG, "标题: " + article.title);

                    // 转换 WebScraper.Article 为 HomeArticle 并添加到列表
                    articleList.add(new HomeArticle(
                            article.title, article.url, article.imageUrl,
                            article.description, article.author, article.date, article.category
                    ));
                }
            }

            // 爬取完成后更新 UI
            runOnUiThread(() -> articleAdapter.notifyDataSetChanged());
        }).start();
    }
}
