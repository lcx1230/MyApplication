package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.ArticleComment;
import com.example.myapplication.model.User;
import com.example.myapplication.dao.UserDAO;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<ArticleComment> commentList;
    private Context context;
    private UserDAO userDAO;

    public CommentAdapter(Context context, List<ArticleComment> commentList) {
        this.context = context;
        this.commentList = commentList;
        this.userDAO = new UserDAO(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArticleComment comment = commentList.get(position);

        // 通过userId获取用户信息
        User user = userDAO.getUserById(comment.getUserId());
        if (user != null) {
            holder.tvUserId.setText(user.getName() != null ? user.getName() : "未知用户");
            // 使用 Glide 加载头像
            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.default_avatar) // 加载中显示默认头像
                    .error(R.drawable.default_avatar)       // 加载失败也显示默认头像
                    .into(holder.ivUserAvatar);
        } else {
            holder.tvUserId.setText("未知用户");
            holder.ivUserAvatar.setImageResource(R.drawable.default_avatar);
        }

        holder.tvCommentContent.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserAvatar;
        TextView tvUserId;
        TextView tvCommentContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
        }
    }
}
