package com.example.myapplication.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.dao.ArticleCommentDAO;
import com.example.myapplication.dao.ArticleDAO;
import com.example.myapplication.dialogs.CommentBottomSheetDialog;
import com.example.myapplication.model.Article;
import com.example.myapplication.utils.PreferenceHelper;
import com.example.myapplication.utils.UserManager;

public class ReadingFragment extends Fragment {

    private TextView tvTitle, tvAuthor, tvLine1, tvLine2, tvLine3, tvLine4;
    private ImageView ivLike, ivComment;
    private TextView tvLikeCount, tvCommentCount;
    private ArticleDAO articleDAO;
    private ArticleCommentDAO articleCommentDAO;
    private Article currentArticle; // 当前显示的文章对象
    private boolean isLiked = false; // 是否已点赞
    private PreferenceHelper preferenceHelper;
    private int userId;

    public ReadingFragment() {
        // 默认构造器
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);

        // 初始化控件
        tvTitle = view.findViewById(R.id.tv_title);
        tvAuthor = view.findViewById(R.id.tv_author);
        tvLine1 = view.findViewById(R.id.tv_line1);
        tvLine2 = view.findViewById(R.id.tv_line2);
        tvLine3 = view.findViewById(R.id.tv_line3);
        tvLine4 = view.findViewById(R.id.tv_line4);
        ivLike = view.findViewById(R.id.iv_like);
        ivComment = view.findViewById(R.id.iv_comment);
        tvLikeCount = view.findViewById(R.id.tv_like_count);
        tvCommentCount = view.findViewById(R.id.tv_comment_count);

        // 从 UserManager 获取用户 ID
        articleDAO = new ArticleDAO(requireContext());
        articleCommentDAO = new ArticleCommentDAO(requireContext());
        // 获取当前用户ID
        userId = UserManager.getInstance().getUser().getUserId();
        // 初始化 SharedPreferences 帮助类
        preferenceHelper = new PreferenceHelper(requireContext());


        loadArticle(); // 加载文章内容
        loadLikeStatus(); // 加载点赞状态
        setupListeners(); // 设置点赞监听

        return view;
    }

    private void loadLikeStatus() {
        // 获取用户的点赞状态
        isLiked = preferenceHelper.getLikeStatus(String.valueOf(userId));

        // 根据点赞状态更新 UI
        if (isLiked) {
            ivLike.setImageResource(R.drawable.like); // 点赞状态的图标
        } else {
            ivLike.setImageResource(R.drawable.unlike); // 默认图标
        }
    }

    private void loadArticle() {
        // 这里为了简单，先加载数据库中的第一篇文章（你后续可以改成随机选一篇或者指定ID）
        currentArticle = articleDAO.getFirstArticle();

        if (currentArticle != null) {
            tvTitle.setText(currentArticle.getTitle());
            tvAuthor.setText("—— " + currentArticle.getAuthorName());

            // 解析内容
            String[] lines = currentArticle.getContent().split(", ");
            if (lines.length >= 4) {
                tvLine1.setText(lines[0]);
                tvLine2.setText(lines[1]);
                tvLine3.setText(lines[2]);
                tvLine4.setText(lines[3]);
            }
            currentArticle.setCommentCount(articleCommentDAO.getCommentCountByArticleId(currentArticle.getArticleId()));
            tvLikeCount.setText(String.valueOf(currentArticle.getLikeCount()));
            tvCommentCount.setText(String.valueOf(currentArticle.getCommentCount()));
        }
    }

    private void setupListeners() {
        ivLike.setOnClickListener(v -> {
            if (currentArticle != null) {
                int currentLikeCount = currentArticle.getLikeCount();

                if (!isLiked) {
                    // 点赞 +1
                    int newLikeCount = currentLikeCount + 1;
                    currentArticle.setLikeCount(newLikeCount);
                    articleDAO.updateLikeCount(currentArticle.getArticleId(), newLikeCount);
                    tvLikeCount.setText(String.valueOf(newLikeCount));
                    ivLike.setImageResource(R.drawable.like); // 已点赞图标
                    isLiked = true;
                    // 更新 SharedPreferences 中的点赞状态
                    preferenceHelper.saveLikeStatus(String.valueOf(userId), true);
                } else {
                    // 取消点赞 -1
                    int newLikeCount = Math.max(0, currentLikeCount - 1);
                    currentArticle.setLikeCount(newLikeCount);
                    articleDAO.updateLikeCount(currentArticle.getArticleId(), newLikeCount);
                    tvLikeCount.setText(String.valueOf(newLikeCount));
                    ivLike.setImageResource(R.drawable.unlike); // 未点赞图标
                    isLiked = false;
                    // 更新 SharedPreferences 中的点赞状态
                    preferenceHelper.saveLikeStatus(String.valueOf(userId), false);
                }
            }
        });

        ivComment.setOnClickListener(v -> {
            if (currentArticle != null) {
                CommentBottomSheetDialog dialog = new CommentBottomSheetDialog(requireContext(), currentArticle.getArticleId(), userId);
                dialog.show();
            }
        });

    }
}
