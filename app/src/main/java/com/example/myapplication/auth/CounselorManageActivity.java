package com.example.myapplication.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CounselorAdapter;
import com.example.myapplication.dao.CounselorDAO;
import com.example.myapplication.model.Counselor;
import java.util.ArrayList;
import java.util.List;

public class CounselorManageActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView counselorRecyclerView;
    private ImageView addCounselorButton;
    private CounselorAdapter adapter;
    private List<Counselor> counselorList = new ArrayList<>();
    private CounselorDAO counselorDAO;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_manage);

        // 绑定控件
        searchEditText = findViewById(R.id.searchBox);
        counselorRecyclerView = findViewById(R.id.counselorRecyclerView);
        addCounselorButton = findViewById(R.id.addCounselorButton);

        // 初始化数据库 DAO
        counselorDAO = new CounselorDAO(this);

        // 加载数据
        loadCounselors();

        // 设置适配器
        adapter = new CounselorAdapter(this, counselorList);
        counselorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        counselorRecyclerView.setAdapter(adapter);

        // 设置长按监听
        setupGestureListener();

        // ➕ 添加按钮
        addCounselorButton.setOnClickListener(v -> {
            Intent intent = new Intent(CounselorManageActivity.this, AddCounselorActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 🔄 加载咨询师数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadCounselors(); // 确保返回时刷新数据
    }

    private void loadCounselors() {
        counselorList.clear();
        counselorList.addAll(counselorDAO.getAllCounselorsWithCertificate());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 🎭 监听手势（单击显示证书，长按弹出菜单）
     */
    private void setupGestureListener() {
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View child = counselorRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int position = counselorRecyclerView.getChildAdapterPosition(child);
                    showCertificateDialog(counselorList.get(position).getCertificateUrl());
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = counselorRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int position = counselorRecyclerView.getChildAdapterPosition(child);
                    showPopupMenu(child, position);
                }
            }
        });

        counselorRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return gestureDetector.onTouchEvent(e);
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
    }

    /**
     * 📜 显示资格证书对话框
     */
    private void showCertificateDialog(String certificateUrl) {
        if (certificateUrl == null || certificateUrl.isEmpty()) {
            Toast.makeText(this, "该咨询师暂无资格证书", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("资格证书");

        // 创建 ImageView 显示证书
        ImageView imageView = new ImageView(this);
        Glide.with(this)
                .load(certificateUrl)
                .placeholder(R.drawable.emoji) // 加载中占位图
                .error(R.drawable.emoji) // 加载失败
                .into(imageView);

        builder.setView(imageView);
        builder.setPositiveButton("关闭", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * 📌 长按弹出菜单
     */
    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.counselor_manage_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            Counselor selectedCounselor = counselorList.get(position);
            if (item.getItemId() == R.id.menu_edit) {
                // ✏️ 进入修改页面
                Intent intent = new Intent(CounselorManageActivity.this, EditCounselorActivity.class);
                intent.putExtra("counselor_id", selectedCounselor.getCounselorId());
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.menu_delete) {
                // ❌ 删除
                confirmDeleteCounselor(selectedCounselor.getCounselorId(), position);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    /**
     * ❌ 确认删除咨询师
     */
    private void confirmDeleteCounselor(int counselorId, int position) {
        new AlertDialog.Builder(this)
                .setTitle("删除确认")
                .setMessage("确定要删除该咨询师吗？此操作无法撤销！")
                .setPositiveButton("删除", (dialog, which) -> {
                    // 删除咨询师
                    counselorDAO.deleteCounselor(counselorId);
                    counselorList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "咨询师已删除", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss()) // 取消删除
                .show();
    }
}
