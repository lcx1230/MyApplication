package com.example.myapplication.utils;

import android.os.Handler;
import android.util.Log;
import com.example.myapplication.model.HomeArticle;
import java.util.ArrayList;
import java.util.List;

public class WebScraperHelper {
    private static final String TAG = "WebScraperHelper";
    private static final Handler handler = new Handler();

    // 定义回调接口
    public interface ArticleCallback {
        void onArticlesFetched(List<HomeArticle> articles);
    }

    // 统一爬取方法
    public static void fetchArticles(int maxPages, ArticleCallback callback) {
        new Thread(() -> {
            List<HomeArticle> homeArticles = new ArrayList<>();
            for (int i = 1; i <= maxPages; i++) {
                String url = "https://www.xinli001.com/info?page=" + i;
                Log.d(TAG, "正在爬取页面: " + url);
                List<WebScraper.Article> articles = WebScraper.getArticles(url);

                if (articles != null) {
                    for (WebScraper.Article article : articles) {
                        homeArticles.add(new HomeArticle(
                                article.title, article.url, article.imageUrl,
                                article.description, article.author, article.date, article.category
                        ));
                    }
                }
            }
            // 回到主线程更新 UI
            handler.post(() -> callback.onArticlesFetched(homeArticles));
        }).start();
    }
}
