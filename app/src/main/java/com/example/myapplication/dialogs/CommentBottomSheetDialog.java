package com.example.myapplication.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.dao.AppointmentDAO;
import com.example.myapplication.dao.ArticleCommentDAO;
import com.example.myapplication.model.ArticleComment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;

public class CommentBottomSheetDialog extends BottomSheetDialog {

    private RecyclerView recyclerView;
    private EditText editTextComment;
    private TextView btnSend;
    private CommentAdapter commentAdapter;
    private List<ArticleComment> commentList = new ArrayList<>();
    private int articleId;
    private int userId;
    private ArticleCommentDAO commentDAO;

    public CommentBottomSheetDialog(@NonNull Context context, int articleId, int userId) {
        super(context);
        this.articleId = articleId;
        this.userId = userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_bottom_comment, null);
        setContentView(view);

        recyclerView = view.findViewById(R.id.recycler_view_comments);
        editTextComment = view.findViewById(R.id.et_input_comment);
        btnSend = view.findViewById(R.id.btn_send_comment);
        commentDAO = new ArticleCommentDAO(getContext());
        // 设置 RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdapter = new CommentAdapter(getContext(),commentList);
        recyclerView.setAdapter(commentAdapter);

        loadComments(); // 加载已有评论

        btnSend.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // 1. 先构建评论对象
                ArticleComment newComment = new ArticleComment();
                newComment.setUserId(userId);
                newComment.setArticleId(articleId);
                newComment.setComment(commentText);
                // 2. 插入到数据库
                long insertedId = commentDAO.insertArticleComment(newComment);


                if (insertedId != -1) {
                    // 3. 插入成功，把ID也赋值回去
                    newComment.setCommentId((int) insertedId);
                    // 4. 加到列表的顶部（位置 0）
                    commentList.add(0, newComment);  // 添加到顶部
                    commentAdapter.notifyItemInserted(0);  // 刷新第一个项
                    recyclerView.scrollToPosition(0);  // 滚动到顶部
                    // 5. 清空输入框
                    editTextComment.setText("");
                }
            }
        });
    }

    private void loadComments() {
        if (commentDAO == null) {
            commentDAO = new ArticleCommentDAO(getContext());
        }
        // 1. 从数据库查
        List<ArticleComment> loadedComments = commentDAO.getCommentsByArticleId(articleId);

        // 2. 先清空旧数据
        commentList.clear();
        if (loadedComments != null && !loadedComments.isEmpty()) {
            commentList.addAll(loadedComments);
        }

        // 3. 通知适配器刷新
        commentAdapter.notifyDataSetChanged();

    }

}
