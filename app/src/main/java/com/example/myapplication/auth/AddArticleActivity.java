package com.example.myapplication.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.dao.ArticleDAO;
import com.example.myapplication.model.Article;

public class AddArticleActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etLine1, etLine2, etLine3, etLine4;
    private Button btnSave;
    private ArticleDAO articleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poetry);

        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etLine1 = findViewById(R.id.et_line1);
        etLine2 = findViewById(R.id.et_line2);
        etLine3 = findViewById(R.id.et_line3);
        etLine4 = findViewById(R.id.et_line4);
        btnSave = findViewById(R.id.btn_save_poetry);

        articleDAO = new ArticleDAO(this);

        btnSave.setOnClickListener(v -> savePoetry());
    }

    private void savePoetry() {
        String title = etTitle.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String line1 = etLine1.getText().toString().trim();
        String line2 = etLine2.getText().toString().trim();
        String line3 = etLine3.getText().toString().trim();
        String line4 = etLine4.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty() || line1.isEmpty() || line2.isEmpty() || line3.isEmpty() || line4.isEmpty()) {
            Toast.makeText(this, "请完整填写所有内容", Toast.LENGTH_SHORT).show();
            return;
        }

        // 拼接内容：使用逗号分割每一行
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(line1).append(", ")
                .append(line2).append(", ")
                .append(line3).append(", ")
                .append(line4);

        Article article = new Article();
        article.setTitle(title);
        article.setAuthorName(author); // 使用 setAuthorName 设置作者名
        article.setContent(contentBuilder.toString());
        article.setLikeCount(0);
        article.setCommentCount(0);

        long newId = articleDAO.insertArticle(article);

        if (newId > 0) {
            Toast.makeText(this, "添加成功！", Toast.LENGTH_SHORT).show();
            finish(); // 返回上一页
        } else {
            Toast.makeText(this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
