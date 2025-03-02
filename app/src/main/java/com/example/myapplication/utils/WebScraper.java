package com.example.myapplication.utils;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {
    private static final String TAG = "WebScraper";

    public static class Article {
        public String title, url, imageUrl, description, author, date, category;

        public Article(String title, String url, String imageUrl, String description, String author, String date, String category) {
            this.title = title;
            this.url = url;
            this.imageUrl = imageUrl;
            this.description = description;
            this.author = author;
            this.date = date;
            this.category = category;
        }
    }

    // 获取文章列表
    public static List<Article> getArticles(String url) {
        List<Article> articles = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements items = doc.select("#articleListM .item");

            for (Element item : items) {
                Element titleElement = item.selectFirst(".title");
                String title = (titleElement != null) ? titleElement.text() : "无标题";
                String articleUrl = (titleElement != null) ? titleElement.absUrl("href") : "#";

                Element imgElement = item.selectFirst("img");
                String imageUrl = (imgElement != null) ? imgElement.absUrl("src") : "";

                Element descElement = item.selectFirst(".desc");
                String description = (descElement != null) ? descElement.text() : "";

                Element authorElement = item.selectFirst(".info a span");
                String author = (authorElement != null) ? authorElement.text() : "未知";

                Element dateElement = item.selectFirst(".info .date");
                String date = (dateElement != null) ? dateElement.text() : "未知";

                Element categoryElement = item.selectFirst(".info a[href*='theme']");
                String category = (categoryElement != null) ? categoryElement.text() : "未分类";

                articles.add(new Article(title, articleUrl, imageUrl, description, author, date, category));
            }
        } catch (IOException e) {
            Log.e(TAG, "请求网页失败: " + e.getMessage());
        }
        return articles;
    }
}
