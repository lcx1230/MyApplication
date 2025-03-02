package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.HomeArticle;
import java.util.List;

public class HomeArticleAdapter extends RecyclerView.Adapter<HomeArticleAdapter.HomeArticleViewHolder> {

    private final Context context;
    private final List<HomeArticle> articleList;

    public HomeArticleAdapter(Context context, List<HomeArticle> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public HomeArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_article, parent, false);
        return new HomeArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeArticleViewHolder holder, int position) {
        HomeArticle article = articleList.get(position);

        // 标题超过 20 个字符省略
        holder.title.setText(article.getTitle().length() > 20 ? article.getTitle().substring(0, 20) + "..." : article.getTitle());
        holder.description.setText(article.getDescription());
        holder.info.setText(article.getAuthor() + " | " + article.getDate() + " | " + article.getCategory());

        // 加载文章图片
        Glide.with(context).load(article.getImageUrl()).placeholder(R.drawable.home).into(holder.imageView);

        // 点击文章跳转到网页
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getLink()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class HomeArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, description, info;

        public HomeArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.articleImage);
            title = itemView.findViewById(R.id.articleTitle);
            description = itemView.findViewById(R.id.articleDescription);
            info = itemView.findViewById(R.id.articleInfo);
        }
    }
}
